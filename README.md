# Centralized QA Data Platform

A Spring Boot application for storing, managing, searching, and maintaining centralized QA test data across environments such as QA, UAT, and PROD.

## Project Goal

The goal of this project is to build a centralized platform where QA and test teams can:

- Store reusable test data in a structured way
- Search test data quickly using different filters
- Manage environment-specific test values
- Import bulk test data from Excel files
- Support controlled delete workflows through admin approval
- Reduce duplication of test data across teams and test cycles
- Improve test-data traceability and maintenance

## What We Have Developed So Far

### Backend Foundation
- Spring Boot application setup
- REST API architecture using controller, service, repository, and model layers
- PostgreSQL database integration using Spring Data JPA
- CORS configuration to allow frontend/API integration
- Multipart file upload support for Excel imports

### Test Data Management
- `TestData` entity with fields such as:
  - category
  - fieldName
  - fieldValue
  - environment
  - country
  - validationType
  - status
  - deleteStatus
  - tag

### CRUD and Data Operations
- Fetch all active test data
- Create new test data records
- Update existing test data records
- Soft delete request flow by marking records as `PENDING`
- Admin approval endpoint for final deletion

### Search and Filter Features
- Filter by environment and country
- Search by field value
- Search by field name and environment
- Combined search using field name, environment, and country
- Search by tag and environment
- View records with pending delete requests

### Excel Upload Support
- Upload Excel files through API
- Parse Excel data using Apache POI
- Validate Excel header structure
- Validate mandatory fields
- Validate allowed environment values (`QA`, `UAT`, `PROD`)
- Skip empty rows
- Save validated records into the database

### Basic Frontend Prototype
- A simple HTML page for:
  - loading all records
  - searching records
  - viewing pending delete requests
  - requesting delete
  - approving delete
- This UI currently exists as a standalone prototype and is not yet fully integrated into Spring static resources

## Tech Stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Apache POI
- Maven
- HTML, CSS, JavaScript

## Current API Overview

Base URL:

```text
http://localhost:8080/testdata
```

Available endpoints:

- `GET /testdata`  
  Get all non-pending records

- `POST /testdata`  
  Create a new record

- `PUT /testdata/{id}`  
  Update an existing record

- `DELETE /testdata/{id}`  
  Request delete by setting `deleteStatus` to `PENDING`

- `DELETE /testdata/admin/{id}`  
  Admin-approved permanent delete

- `GET /testdata/filter?environment=QA&country=India`  
  Filter by environment and country

- `GET /testdata/search?...`  
  Search using field name, environment, and/or country

- `GET /testdata/searchByTag?tag=login_valid_user&environment=QA`  
  Search using tag and environment

- `GET /testdata/pending`  
  Get all pending delete requests

- `POST /testdata/upload`  
  Upload Excel file and save data to database

## Database Configuration

Current database configuration is set in `application.properties`:

- PostgreSQL database: `testdata_db`
- Default username: `postgres`
- Default password: `postgres`

## Project Structure

```text
src/
  main/
    java/com/example/demo/
      config/
      controller/
      model/
      repository/
      service/
    resources/
      application.properties
      static/
      templates/
  test/
    java/com/example/demo/
```

## Current Limitations

- Only a basic context-load test is present
- No authentication or role-based authorization yet
- Admin delete approval is handled through open endpoints
- Frontend is a basic prototype and not yet fully integrated
- Error handling is still minimal
- No API documentation such as Swagger/OpenAPI yet

## Suggested Next Steps

- Integrate the frontend into the Spring Boot application
- Add proper validation and structured error responses
- Add unit and integration tests
- Add authentication and admin authorization
- Improve Excel upload reporting with row-level error details
- Add pagination and sorting for large datasets
- Add API documentation
- Add audit/history tracking for test data changes

## How to Run

### Prerequisites
- Java 17
- Maven
- PostgreSQL

### Steps
1. Create a PostgreSQL database named `testdata_db`
2. Update database credentials in `src/main/resources/application.properties` if needed
3. Run the application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

4. Access the API at:

```text
http://localhost:8080/testdata
```

## Status

This project currently provides a working backend for centralized QA test data storage, search, filtering, Excel-based import, and soft-delete approval flow, with a basic frontend prototype prepared for future integration.