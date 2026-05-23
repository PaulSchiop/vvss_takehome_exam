# Task 4: Advanced Contract Functionality Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement "Sign Contract" logic and "Team Payroll Report" with full test coverage and validation.

**Architecture:** Advanced MVC with DTOs and Service layer integration. Cross-entity relationships between Player, Team, and Contract.

**Tech Stack:** Java 17, Spring Boot 3, JPA/Hibernate, Lombok, JUnit 5, Mockito, Bean Validation.

---

### Task 4.1: Define DTOs

**Files:**
- Create: `src/main/java/com/vvss/exam/dto/ContractRequestDTO.java`
- Create: `src/main/java/com/vvss/exam/dto/TeamReportDTO.java`

- [ ] **Step 1: Create ContractRequestDTO**
```java
package com.vvss.exam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDTO {
    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be strictly positive")
    private Integer salary;
}
```

- [ ] **Step 2: Create TeamReportDTO**
```java
package com.vvss.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamReportDTO {
    private String teamName;
    private List<ContractDTO> contracts;
    private Integer totalPayroll;
}
```

- [ ] **Step 3: Commit DTOs**
```bash
git add src/main/java/com/vvss/exam/dto/ContractRequestDTO.java src/main/java/com/vvss/exam/dto/TeamReportDTO.java
git commit -m "feat: add ContractRequestDTO and TeamReportDTO"
```

---

### Task 4.2: Implement ContractService logic (Part 1 - Mapping and Reporting)

**Files:**
- Modify: `src/main/java/com/vvss/exam/service/ContractService.java`
- Modify: `src/main/java/com/vvss/exam/service/PlayerService.java` (optional, to expose repository or entity fetcher)
- Modify: `src/main/java/com/vvss/exam/service/TeamService.java` (optional)

*Note: Since the instructions say to use `playerService.findById` and `teamService.findById` but they return DTOs, I will add `findEntityById` to those services or use repositories if needed. I'll add `findEntityById` to stay clean.*

- [ ] **Step 1: Add findEntityById to PlayerService**
```java
// src/main/java/com/vvss/exam/service/PlayerService.java
public Player findEntityById(Long id) {
    return playerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
}
```

- [ ] **Step 2: Add findEntityById to TeamService**
```java
// src/main/java/com/vvss/exam/service/TeamService.java
public Team findEntityById(Long id) {
    return teamRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
}
```

- [ ] **Step 3: Update ContractService with dependencies and mapToDTO**
```java
// src/main/java/com/vvss/exam/service/ContractService.java
private final PlayerService playerService;
private final TeamService teamService;

private ContractDTO mapToDTO(Contract contract) {
    PlayerDTO playerDTO = new PlayerDTO(contract.getPlayer().getId(), contract.getPlayer().getName(), contract.getPlayer().getPosition());
    TeamDTO teamDTO = new TeamDTO(contract.getTeam().getId(), contract.getTeam().getCity(), contract.getTeam().getName());
    return new ContractDTO(contract.getId(), contract.getSalary(), playerDTO, teamDTO);
}
```

- [ ] **Step 4: Implement getTeamPayrollReport in ContractService**
```java
public TeamReportDTO getTeamPayrollReport(Long teamId) {
    TeamDTO team = teamService.findById(teamId);
    List<Contract> contracts = contractRepository.findByTeamId(teamId);
    List<ContractDTO> contractDTOs = contracts.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    Integer totalPayroll = contracts.stream()
            .map(Contract::getSalary)
            .reduce(0, Integer::sum);
    
    return new TeamReportDTO(team.getName(), contractDTOs, totalPayroll);
}
```

---

### Task 4.3: Implement ContractService logic (Part 2 - signFreeAgent)

- [ ] **Step 1: Implement signFreeAgent in ContractService**
```java
public ContractDTO signFreeAgent(ContractRequestDTO request) {
    // Ensuring they exist via findById as requested (throws exception if not found)
    playerService.findById(request.getPlayerId());
    teamService.findById(request.getTeamId());
    
    // Fetching entities for linking
    Player player = playerService.findEntityById(request.getPlayerId());
    Team team = teamService.findEntityById(request.getTeamId());
    
    Contract contract = new Contract();
    contract.setPlayer(player);
    contract.setTeam(team);
    contract.setSalary(request.getSalary());
    
    Contract savedContract = contractRepository.save(contract);
    return mapToDTO(savedContract);
}
```

---

### Task 4.4: Write ContractService Tests

**Files:**
- Create: `src/test/java/com/vvss/exam/service/ContractServiceTest.java`

- [ ] **Step 1: Create the test class with mocks**
```java
package com.vvss.exam.service;

import com.vvss.exam.dto.*;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private ContractService contractService;

    @Test
    void signFreeAgent_Success() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, 50000);
        Player player = new Player(1L, "Player 1", "ST");
        Team team = new Team(1L, "City 1", "Team 1");
        Contract contract = new Contract(1L, 50000, player, team);

        when(playerService.findById(1L)).thenReturn(new PlayerDTO());
        when(teamService.findById(1L)).thenReturn(new TeamDTO());
        when(playerService.findEntityById(1L)).thenReturn(player);
        when(teamService.findEntityById(1L)).thenReturn(team);
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        ContractDTO result = contractService.signFreeAgent(request);

        assertNotNull(result);
        assertEquals(50000, result.getSalary());
        assertEquals("Player 1", result.getPlayerDTO().getName());
        assertEquals("Team 1", result.getTeamDTO().getName());
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void signFreeAgent_PlayerNotFound() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, 50000);
        when(playerService.findById(1L)).thenThrow(new ResourceNotFoundException("Player not found"));

        assertThrows(ResourceNotFoundException.class, () -> contractService.signFreeAgent(request));
    }

    @Test
    void getTeamPayrollReport_Success() {
        Long teamId = 1L;
        TeamDTO teamDTO = new TeamDTO(teamId, "City 1", "Team 1");
        Player player = new Player(1L, "Player 1", "ST");
        Team team = new Team(teamId, "City 1", "Team 1");
        Contract c1 = new Contract(1L, 10000, player, team);
        Contract c2 = new Contract(2L, 20000, player, team);

        when(teamService.findById(teamId)).thenReturn(teamDTO);
        when(contractRepository.findByTeamId(teamId)).thenReturn(List.of(c1, c2));

        TeamReportDTO report = contractService.getTeamPayrollReport(teamId);

        assertEquals("Team 1", report.getTeamName());
        assertEquals(2, report.getContracts().size());
        assertEquals(30000, report.getTotalPayroll());
    }
}
```

- [ ] **Step 2: Run the test**
Run: `mvn test -Dtest=ContractServiceTest`

---

### Task 4.5: Update ContractController

**Files:**
- Modify: `src/main/java/com/vvss/exam/controller/ContractController.java`

- [ ] **Step 1: Update signFreeAgentForm**
```java
@GetMapping("/sign")
public String signFreeAgentForm(Model model) {
    model.addAttribute("contractRequest", new ContractRequestDTO()); // Changed from Contract
    model.addAttribute("players", playerService.findAll());
    model.addAttribute("teams", teamService.findAll());
    return "contracts/sign";
}
```

- [ ] **Step 2: Update signFreeAgent**
```java
@PostMapping("/sign")
public String signFreeAgent(@Valid @ModelAttribute("contractRequest") ContractRequestDTO request, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("players", playerService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "contracts/sign";
    }
    contractService.signFreeAgent(request);
    return "redirect:/contracts/report";
}
```

- [ ] **Step 3: Update reportForm**
```java
@GetMapping("/report")
public String reportForm(Model model, @RequestParam(required = false) Long teamId) {
    model.addAttribute("teams", teamService.findAll());
    if (teamId != null) {
        try {
            TeamReportDTO report = contractService.getTeamPayrollReport(teamId);
            model.addAttribute("report", report);
        } catch (ResourceNotFoundException e) {
            // Team not found
        }
    }
    return "contracts/report";
}
```

---

### Task 4.6: Verification and Commit

- [ ] **Step 1: Run all tests**
Run: `mvn test`

- [ ] **Step 2: Final commit**
```bash
git add .
git commit -m "feat: implement advanced contract logic and reporting"
```
