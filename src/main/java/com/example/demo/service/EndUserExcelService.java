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

import com.example.demo.model.EndUser;

@Service
public class EndUserExcelService {

    public List<EndUser> parseExcel(InputStream is) throws Exception {

        List<EndUser> list = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        Row header = sheet.getRow(0);

        validateHeader(header);

        for (Row row : sheet) {

            if (row.getRowNum() == 0) continue;
            if (isRowEmpty(row)) continue;

            EndUser data = new EndUser();

            data.setEndUserName(getCellValue(row.getCell(0)));
            data.setMarketSegmentType(getCellValue(row.getCell(1)));

            data.setAddressLine1(getCellValue(row.getCell(2)));
            data.setAddressLine2(getCellValue(row.getCell(3)));
            data.setAddressLine3(getCellValue(row.getCell(4)));

            data.setCity(getCellValue(row.getCell(5)));
            data.setState(getCellValue(row.getCell(6)));
            data.setPostalCode(getCellValue(row.getCell(7)));

            data.setCountry(getCellValue(row.getCell(8)));

            data.setContactName(getCellValue(row.getCell(9)));
            data.setPhoneNumber(getCellValue(row.getCell(10)));
            data.setEmail(getCellValue(row.getCell(11)));

            String env = getCellValue(row.getCell(12)).toUpperCase();
            validateEnvironment(env);
            data.setEnvironment(env);

            data.setValidationType(getCellValue(row.getCell(13)));
            data.setTag(getCellValue(row.getCell(14)));

            data.setDeleteStatus("NONE");

            list.add(data);
        }

        workbook.close();
        return list;
    }

    // ✅ HEADER VALIDATION
    private void validateHeader(Row header) {

        String[] expectedHeaders = {
                "endUserName", "marketSegmentType", "addressLine1",
                "addressLine2", "addressLine3", "city", "state",
                "postalCode", "country", "contactName",
                "phoneNumber", "email", "environment",
                "validationType", "tag"
        };

        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell cell = header.getCell(i);

            if (cell == null ||
                !expectedHeaders[i].equalsIgnoreCase(cell.getStringCellValue().trim())) {

                throw new RuntimeException(
                        "Invalid Excel format at column: " + expectedHeaders[i]
                );
            }
        }
    }

    // ✅ ENV VALIDATION
    private void validateEnvironment(String env) {
        if (!env.equals("QA") && !env.equals("UAT") && !env.equals("PROD")) {
            throw new RuntimeException("Invalid environment: " + env);
        }
    }

    // ✅ CELL VALUE
    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!getCellValue(row.getCell(i)).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
