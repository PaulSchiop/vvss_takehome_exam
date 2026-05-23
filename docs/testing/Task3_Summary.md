# Task 3: Testing II Summary Report

## 1. Introduction
The objective of Task 3 was to apply advanced testing techniques including Formal Code Inspection, Exploratory Testing, and Automated GUI Testing to ensure the quality and robustness of the contract management system.

## 2. Inspection / Review Summary
A formal inspection of `ContractService.java` and `GlobalExceptionHandler.java` was conducted.
- **Strengths:** Architecturally sound, consistent use of DTOs, and clear JavaDoc documentation.
- **Key Findings:**
    - Redundant database calls in `signFreeAgent`.
    - Lack of business validation for duplicate contracts (one player signed to multiple teams).
    - Limited exception handling in the global handler (missing `IllegalArgumentException` mapping).
- **Recommendations:** Optimize entity fetching, implement duplicate contract checks, and expand the global exception handler to provide better user feedback.

## 3. Exploratory Testing Summary
A 60-minute exploratory session focused on 'Sign Contract' and 'Payroll Report' features.
- **Charter:** Data consistency and UI feedback during invalid operations.
- **Key Findings:**
    - **Bug (Business Logic):** Players can be signed to multiple teams concurrently.
    - **UX Issue:** Post-signing redirection to the report page doesn't automatically filter for the relevant team.
    - **UX Issue:** Missing success confirmation messages after signing a contract.
    - **Technical Issue:** Integer overflow for very large salary values isn't gracefully handled.

## 4. GUI / Web Testing Summary
Automated GUI tests were implemented using `MockMvc` to verify the HTML-based user interface.
- **Tests Covered:**
    - `testRenderSignForm`: Verifies correct rendering of the contract signing form and presence of required fields.
    - `testValidationFeedback`: Confirms that invalid input (e.g., negative salary) triggers appropriate error messages in the HTML view.
    - `testReportRendering`: Ensures that the payroll report table correctly displays data for a specific team.
- **Result:** All GUI tests pass, confirming basic UI integrity and validation feedback loops.

## 5. Conclusion
Testing II revealed that while the system is functionally stable, it has gaps in business rule enforcement and user experience. The combination of manual inspection and exploratory testing was highly effective at identifying subtle logical flaws that automated tests (which follow the current implementation) did not catch. The automated GUI tests provide a safety net for future UI changes.
