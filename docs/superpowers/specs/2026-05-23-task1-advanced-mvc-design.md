# Task 1: Advanced MVC Design for VVSS Take-Home Exam

**Date:** 2026-05-23
**Status:** Approved (via brainstorming)

## Goal
Implement Task 1 requirements (3 CRUD entities, 1 cross-entity functionality, 1 reporting functionality) using an "Advanced MVC" approach. This ensures the codebase is clean, well-layered, and follows modern Spring Boot best practices.

## Architecture

### 1. Data Layer (Entities)
- **Player**: `Long id`, `String name`, `String position`.
- **Team**: `Long id`, `String city`, `String name`.
- **Contract**: `Long id`, `Integer salary`, `Player player`, `Team team`.
- All entities will use Jakarta Persistence (JPA) annotations and Lombok for boilerplate.

### 2. Transfer Layer (DTOs)
- **PlayerDTO**, **TeamDTO**, **ContractDTO**: Used for returning data to the View layer.
- **ContractRequestDTO**: Used for the contract signing form to capture `playerId`, `teamId`, and `salary`.
- **TeamReportDTO**: Specifically for the payroll report, containing the team name and a list of player/salary details.

### 3. Service Layer
- **PlayerService**: CRUD operations, mapping to DTOs.
- **TeamService**: CRUD operations, mapping to DTOs.
- **ContractService**: 
    - Handles `signFreeAgent(ContractRequestDTO)` (cross-entity logic).
    - Generates `TeamReportDTO` (reporting logic).
- Services will throw custom exceptions like `ResourceNotFoundException`.

### 4. Web Layer (Controllers)
- **PlayerController**: `/players/**`
- **TeamController**: `/teams/**`
- **ContractController**: `/contracts/**`
- Controllers will handle only View orchestration and form binding.

### 5. Error Handling
- **GlobalExceptionHandler**: Annotated with `@ControllerAdvice`.
- Methods to handle `ResourceNotFoundException`, `MethodArgumentNotValidException`, and general `Exception`.
- Redirects to a generic error page or re-renders forms with error messages.

## Key Functionalities

1.  **3 CRUD Entities**: Players, Teams, and Contracts will have full Create, Read, Update, Delete functionality.
2.  **Cross-Entity Functionality**: "Sign Contract" - Links a Player to a Team via a Contract with a specific salary.
3.  **Reporting**: "Team Payroll Report" - Aggregates all contracts for a specific team and calculates the total payroll.

## Validation
- Bean Validation (`@NotNull`, `@Size`, `@Positive`, etc.) on DTOs and Entities.
- Business validation in Services (e.g., checking if player/team exists before signing).

## Documentation
- JavaDoc on all public Service methods and Controllers.
- Clear naming conventions.
