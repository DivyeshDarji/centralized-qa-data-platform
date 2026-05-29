package com.example.demo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.TestData;

@Service
public class ExcelService {

    public List<TestData> parseExcel(InputStream is) throws Exception {

        List<TestData> list = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        // ✅ STEP 1: Validate Header
        Row header = sheet.getRow(0);

        if (header == null) {
            throw new RuntimeException("Excel file is empty or missing header row");
        }

        validateHeader(header);

        // ✅ STEP 2: Process Rows
        for (Row row : sheet) {

            if (row.getRowNum() == 0)
                continue; // skip header

            // ✅ Skip empty rows
            if (isRowEmpty(row))
                continue;

            // ✅ Validate mandatory fields
            validateMandatoryFields(row);

            TestData data = new TestData();

            data.setCategory(getCellValue(row.getCell(0)));
            data.setFieldName(getCellValue(row.getCell(1)));
            data.setFieldValue(getCellValue(row.getCell(2)));

            // ✅ Validate Environment
            String env = getCellValue(row.getCell(3)).toUpperCase();
            validateEnvironment(env);
            data.setEnvironment(env);

            data.setCountry(getCellValue(row.getCell(4)));

            String validationType = getCellValue(row.getCell(5));
            data.setValidationType(validationType);

            String status = getCellValue(row.getCell(6));
            data.setStatus(status.isEmpty() ? "ACTIVE" : status);

            String tag = getCellValue(row.getCell(7));
            data.setTag(tag);

            // ✅ Default delete status
            data.setDeleteStatus("NONE");

            list.add(data);
        }

        workbook.close();
        return list;
    }

    // ✅ HEADER VALIDATION
    private void validateHeader(Row header) {

        String[] expectedHeaders = {
                "category",
                "fieldName",
                "fieldValue",
                "environment",
                "country",
                "validationType",
                "status",
                "tag"
        };

        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell cell = header.getCell(i);

            if (cell == null ||
                    !expectedHeaders[i].equalsIgnoreCase(cell.getStringCellValue().trim())) {

                throw new RuntimeException(
                        "Invalid Excel format. Expected column: " + expectedHeaders[i] + " at index " + i);
            }
        }
    }

    // ✅ MANDATORY FIELD VALIDATION
    private void validateMandatoryFields(Row row) {

        if (isEmpty(row.getCell(0)) || isEmpty(row.getCell(1)) ||
                isEmpty(row.getCell(2)) || isEmpty(row.getCell(3))) {

            throw new RuntimeException(
                    "Missing mandatory fields at row: " + (row.getRowNum() + 1));
        }
    }

    // ✅ ENVIRONMENT VALIDATION
    private void validateEnvironment(String env) {

        if (!env.equals("QA") && !env.equals("UAT") && !env.equals("PROD")) {
            throw new RuntimeException("Invalid environment: " + env);
        }
    }

    // ✅ CELL VALUE EXTRACTOR
    private String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getStringCellValue();

            default:
                return "";
        }
    }

    // ✅ CHECK EMPTY CELL
    private boolean isEmpty(Cell cell) {
        return cell == null || getCellValue(cell).isEmpty();
    }

    // ✅ CHECK EMPTY ROW
    private boolean isRowEmpty(Row row) {

        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!isEmpty(row.getCell(i))) {
                return false;
            }
        }
        return true;
    }
}
