# Task 1: Advanced MVC Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement Task 1 requirements (3 CRUD entities, 1 cross-entity functionality, 1 reporting functionality) using an "Advanced MVC" approach.

**Architecture:** Strict layering with DTOs, Services with business logic, and custom Exception handling.

**Tech Stack:** Spring Boot, Spring Data JPA, H2, Thymeleaf, Lombok, Bean Validation.

---

### Task 1: Exception Handling & DTO Infrastructure

**Files:**
- Create: `src/main/java/com/vvss/exam/exception/ResourceNotFoundException.java`
- Create: `src/main/java/com/vvss/exam/exception/GlobalExceptionHandler.java`
- Create: `src/main/java/com/vvss/exam/dto/PlayerDTO.java`
- Create: `src/main/java/com/vvss/exam/dto/TeamDTO.java`
- Create: `src/main/java/com/vvss/exam/dto/ContractDTO.java`

- [ ] **Step 1: Create Custom Exception**
Create `ResourceNotFoundException.java` extending `RuntimeException`.

- [ ] **Step 2: Create Global Exception Handler**
Implement `GlobalExceptionHandler.java` with `@ControllerAdvice` to handle `ResourceNotFoundException`.

- [ ] **Step 3: Create DTOs**
Implement `PlayerDTO`, `TeamDTO`, and `ContractDTO` as simple POJOs with Lombok `@Data`.

- [ ] **Step 4: Commit**
`git add src/main/java/com/vvss/exam/exception src/main/java/com/vvss/exam/dto && git commit -m "feat: add exception handling and DTO infrastructure"`

---

### Task 2: Refactor Player Layer to Advanced MVC

**Files:**
- Modify: `src/main/java/com/vvss/exam/service/PlayerService.java`
- Modify: `src/main/java/com/vvss/exam/controller/PlayerController.java`
- Create: `src/test/java/com/vvss/exam/service/PlayerServiceTest.java`

- [ ] **Step 1: Write failing test for PlayerService**
Test that `findById` throws `ResourceNotFoundException` when player is missing.

- [ ] **Step 2: Update PlayerService**
Modify `findAll` to return `List<PlayerDTO>` and `findById` to return `PlayerDTO` (or throw exception). Use a private mapping method.

- [ ] **Step 3: Update PlayerController**
Update controller to use `PlayerDTO` and handle potential exceptions.

- [ ] **Step 4: Verify and Commit**
`mvn test -Dtest=PlayerServiceTest && git add . && git commit -m "refactor: update player layer to use DTOs and exceptions"`

---

### Task 3: Refactor Team Layer to Advanced MVC

**Files:**
- Modify: `src/main/java/com/vvss/exam/service/TeamService.java`
- Modify: `src/main/java/com/vvss/exam/controller/TeamController.java`
- Create: `src/test/java/com/vvss/exam/service/TeamServiceTest.java`

- [ ] **Step 1: Write failing test for TeamService**
Test `findById` exception handling.

- [ ] **Step 2: Update TeamService**
Implement DTO mapping and exception throwing.

- [ ] **Step 3: Update TeamController**
Sync with Service changes.

- [ ] **Step 4: Verify and Commit**
`mvn test -Dtest=TeamServiceTest && git add . && git commit -m "refactor: update team layer to use DTOs and exceptions"`

---

### Task 4: Advanced Contract Functionality (Cross-Entity & Reporting)

**Files:**
- Create: `src/main/java/com/vvss/exam/dto/ContractRequestDTO.java`
- Create: `src/main/java/com/vvss/exam/dto/TeamReportDTO.java`
- Modify: `src/main/java/com/vvss/exam/service/ContractService.java`
- Modify: `src/main/java/com/vvss/exam/controller/ContractController.java`
- Create: `src/test/java/com/vvss/exam/service/ContractServiceTest.java`

- [ ] **Step 1: Define Request and Report DTOs**
`ContractRequestDTO` (form input) and `TeamReportDTO` (aggregated data).

- [ ] **Step 2: Implement Advanced Logic in ContractService**
Implement `signFreeAgent(ContractRequestDTO)` and `getTeamPayrollReport(Long teamId)`.

- [ ] **Step 3: Write tests for ContractService**
Verify payroll calculation and signing logic.

- [ ] **Step 4: Update ContractController**
Update endpoints to use new DTOs and Service methods.

- [ ] **Step 5: Verify and Commit**
`mvn test -Dtest=ContractServiceTest && git add . && git commit -m "feat: implement advanced contract logic and reporting"`

---

### Task 5: Final Cleanup & Documentation

**Files:**
- Modify: `src/main/java/com/vvss/exam/**/*.java`
- Modify: `src/main/resources/templates/**/*.html`

- [ ] **Step 1: Add JavaDocs**
Add comprehensive JavaDocs to all services and controllers.

- [ ] **Step 2: UI Sync**
Ensure all Thymeleaf templates correctly reference DTO fields (e.g., `playerDTO.name` instead of `player.name` if changed).

- [ ] **Step 3: Final Verification Run**
Run all tests and check application startup.

- [ ] **Step 4: Commit**
`git add . && git commit -m "docs: add JavaDocs and final cleanup"`
