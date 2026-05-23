# Task 2: White-Box Testing Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement white-box tests for `ContractService.getTeamPayrollReport` to achieve 100% statement and branch coverage.

**Architecture:** Use JUnit 5 and Mockito to mock dependencies (`TeamService`, `ContractRepository`). Verify all execution paths.

**Tech Stack:** Java, Spring Boot, JUnit 5, Mockito, Maven.

---

### Task 1: WBT Design Documentation

**Files:**
- Create: `docs/testing/Task2_WBT_Design.md`

- [ ] **Step 1: Create the design document**
Write the logic flow, decision points, and identified paths for `getTeamPayrollReport`.

### Task 2: Implement WBT Tests

**Files:**
- Create: `src/test/java/com/vvss/exam/service/ContractServiceWBTTest.java`

- [ ] **Step 1: Create the test class with mocks**
Initialize Mockito and inject mocks into `ContractService`.

- [ ] **Step 2: Path 1 - Team not found**
Test that `ResourceNotFoundException` is propagated when `teamService.findById` fails.

- [ ] **Step 3: Path 2 - Team found, zero contracts**
Test that an empty report is generated when there are no contracts.

- [ ] **Step 4: Path 3 - Team found, multiple contracts**
Test that the report correctly aggregates multiple contracts and calculates the total payroll.

### Task 3: Verification and Commit

- [ ] **Step 1: Run the tests**
Run: `mvn test -Dtest=ContractServiceWBTTest`
Expected: ALL PASS

- [ ] **Step 2: Commit changes**
Run: `git add . && git commit -m "test: add white-box tests and documentation"`
