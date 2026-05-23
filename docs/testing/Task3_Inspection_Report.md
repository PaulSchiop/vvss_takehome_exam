# Task 3: Formal Code Inspection Report

## 1. Overview
**Date:** 2026-05-23
**Inspector:** Gemini CLI
**Files Inspected:**
- `src/main/java/com/vvss/exam/service/ContractService.java`
- `src/main/java/com/vvss/exam/exception/GlobalExceptionHandler.java`

## 2. Inspection Checklist

| ID | Category | Item | Result |
|----|----------|------|--------|
| A1 | Architecture | Follows Advanced MVC (DTO usage)? | Pass |
| A2 | Architecture | Proper service boundaries? | Pass |
| L1 | Logic | Entity relations handled correctly? | Pass |
| L2 | Logic | Payroll calculation accuracy? | Pass |
| L3 | Logic | Duplicate/Invalid state checks? | **Fail** |
| E1 | Error Handling | Custom exceptions thrown where appropriate? | Pass |
| E2 | Error Handling | Global handler captures exceptions? | **Partial** |
| Q1 | Quality | JavaDocs present and descriptive? | Pass |
| Q2 | Quality | Clean code & Java conventions? | Pass |

## 3. Detailed Findings

### 3.1 ContractService.java
**Strengths:**
- Good use of DTOs for input and output, maintaining service boundaries.
- Effective use of `Lombok` for reducing boilerplate.
- JavaDocs are informative and follow standard conventions.

**Issues/Weaknesses:**
- **Redundant Database Calls:** In `signFreeAgent`, `findById` is called on `playerService` and `teamService` just for validation (throwing exception), then `findEntityById` is called again to fetch the entities. This results in 4 DB calls where 2 would suffice if `findEntityById` already handles existence checks.
- **Missing Business Logic Validation:** There is no check to see if a player already has a contract with the same team or another team (assuming one contract per player-team pair or absolute uniqueness).
- **Redundancy in Payroll Logic:** `calculateTeamPayroll` and `getTeamPayrollReport` both contain logic for calculating the sum of salaries. `getTeamPayrollReport` could call `calculateTeamPayroll`.

### 3.2 GlobalExceptionHandler.java
**Strengths:**
- Correct use of `@ControllerAdvice` and `@ExceptionHandler`.
- Maps `ResourceNotFoundException` to a user-friendly error page.

**Issues/Weaknesses:**
- **Limited Exception Coverage:** Only `ResourceNotFoundException` is handled. `IllegalArgumentException` (thrown by `ContractService.signFreeAgent`) is not explicitly handled, which might lead to a generic 500 error page instead of a descriptive message for the user.
- **No Fallback Handler:** Missing a generic `Exception.class` handler to catch unforeseen errors and provide a consistent UI experience.

## 4. Recommendations for Improvement

### 4.1 Improvements for ContractService.java
1. **Optimize Entity Fetching:** Refactor `signFreeAgent` to use only `findEntityById`. Ensure `findEntityById` throws `ResourceNotFoundException` if the entity is missing.
2. **Implement Duplicate Check:** Add a check in `signFreeAgent` to verify if the player is already signed to the team (or any team, depending on league rules) to prevent duplicate contracts.
3. **DRY Payroll Calculation:** Update `getTeamPayrollReport` to utilize `calculateTeamPayroll(teamId)` instead of recalculating the sum manually.

### 4.2 Improvements for GlobalExceptionHandler.java
1. **Handle IllegalArgumentException:** Add an `@ExceptionHandler(IllegalArgumentException.class)` method to return the "error" view with the specific validation message.
2. **Add Generic Handler:** Implement a catch-all `handleGlobalException(Exception ex)` to ensure all errors are logged and presented through the standard error template.

## 5. Conclusion
The current implementation is architecturally sound and follows good coding practices. However, it lacks some robust business logic validations and comprehensive error handling. Implementing the recommendations above will significantly improve the system's reliability and user experience.
