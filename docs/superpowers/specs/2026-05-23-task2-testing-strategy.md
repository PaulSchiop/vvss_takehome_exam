# Task 2: Testing I Strategy - BBT, WBT, Integration

**Date:** 2026-05-23
**Status:** Approved (via brainstorming)

## Goal
Apply formal testing techniques (Black-Box, White-Box, and Integration) to verify the football management application.

## 1. Black-Box Testing (BBT)
We will focus on `ContractService.signFreeAgent(ContractRequestDTO)`.

### Techniques:
- **Equivalence Class Partitioning (ECP)**
- **Boundary Value Analysis (BVA)**

### Partitions & Boundaries:
- **Player ID**: {Valid Existing}, {Invalid Non-existing}, {Null/Negative}.
- **Team ID**: {Valid Existing}, {Invalid Non-existing}, {Null/Negative}.
- **Salary**: {Positive > 0}, {Zero 0}, {Negative < 0}.
    - Boundary: 0 (Lower bound), 1 (Valid min).

### Test Cases:
- TC1: Valid Player, Valid Team, Positive Salary -> Success.
- TC2: Non-existent Player -> ResourceNotFoundException.
- TC3: Non-existent Team -> ResourceNotFoundException.
- TC4: Salary = 0 -> Business/Validation Exception (if applicable) or test persistence behavior.

## 2. White-Box Testing (WBT)
We will focus on `ContractService.getTeamPayrollReport(Long teamId)`.

### Techniques:
- **Statement Coverage**: Ensure every line is executed.
- **Branch Coverage**: Ensure all logical branches (e.g., team existence check) are followed.

### Logic Flow:
1. `teamService.findById(teamId)` -> Potential Branch (Found/Not Found).
2. `contractRepository.findByTeamId(teamId)` -> Potential Branch (Empty list / Non-empty list).
3. `contracts.stream().map(...)` -> Execution of mapping logic.
4. `contracts.stream().map(Contract::getSalary).reduce(...)` -> Execution of aggregation logic.

## 3. Integration Testing
We will perform **Top-Down / Big Bang Integration** using Spring Boot's test infrastructure.

### Target:
- `ContractController` -> `ContractService` -> `Repositories` -> `H2 Database`.

### Scenarios:
- Full flow of "Sign Contract": POST request to `/contracts/sign` should result in a new record in the `CONTRACT` table and a successful redirect to the report page.
- Verification that the Report Page shows the data persisted in the database during the same session.

## 4. Documentation
- A summary document will be created in `docs/testing/Task2_Summary.md`.
- Specific tables for ECP/BVA and CFG (Control Flow Graph) descriptions will be included.
