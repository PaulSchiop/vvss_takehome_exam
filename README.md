# Football Management System - SSVV Take-Home Exam

This project is a Spring Boot application developed for the **Application Development and Testing (SSVV)** take-home exam. It follows an Advanced MVC architecture and includes comprehensive testing documentation.

## Prerequisites

- **Java 17** or higher
- **Maven** (optional, the project includes `./mvnw` wrapper)

## How to Run the Application

1. **Clone the repository** (if not already done).
2. **Start the application**:
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Access the Web Interface**:
   Open your browser and go to `http://localhost:8080`
   - **Players**: `/players`
   - **Teams**: `/teams`
   - **Sign Free Agent**: `/contracts/sign`
   - **Payroll Report**: `/contracts/report`

4. **H2 Console**:
   Access the database console at `http://localhost:8080/h2-console`
   - **JDBC URL**: `jdbc:h2:file:./devdb`
   - **User**: `SA`
   - **Password**: (blank)

## How to Run Tests

To ensure everything is working correctly on your machine, run the full test suite:

```bash
./mvnw test
```

This will execute **29 tests** covering:
- **Unit Tests**: Service layer logic.
- **Black-Box Tests**: ECP and BVA for contract signing.
- **White-Box Tests**: Branch and statement coverage for reporting.
- **Integration Tests**: Full-stack flow from Controller to Database.
- **GUI Tests**: HTML rendering and validation feedback verification.

## Documentation Structure

All required exam deliverables are located in the `docs/` folder:

- **Testing I (BBT, WBT, Integration)**: `docs/testing/Task2_Summary.md`
- **Testing II (Inspection, Exploratory, GUI)**: `docs/testing/Task3_Summary.md`