# Team Layer Refactor to Advanced MVC Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `TeamService` and `TeamController` to use `TeamDTO` and handle `ResourceNotFoundException`.

**Architecture:** Use DTOs for data transfer between layers and custom exceptions for error handling. Mockito for unit testing the service layer.

**Tech Stack:** Java, Spring Boot, JUnit 5, Mockito, Lombok.

---

### Task 1: Write failing test for TeamService

**Files:**
- Create: `src/test/java/com/vvss/exam/service/TeamServiceTest.java`

- [ ] **Step 1: Create TeamServiceTest.java with a failing test for findById**

```java
package com.vvss.exam.service;

import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void findById_WhenTeamNotFound_ShouldThrowResourceNotFoundException() {
        Long teamId = 1L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.findById(teamId));
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -Dtest=TeamServiceTest`
Expected: FAIL because `TeamService.findById` returns `Optional<Team>` and doesn't throw `ResourceNotFoundException`. It might even fail to compile if I changed the signature already, but here I'm writing the test first against current code (or what it should be). Actually, if I write `assertThrows(ResourceNotFoundException.class, () -> teamService.findById(teamId))` but `findById` returns `Optional`, it won't throw it.

- [ ] **Step 3: Commit**

```bash
git add src/test/java/com/vvss/exam/service/TeamServiceTest.java
git commit -m "test: add failing test for TeamService findById"
```

---

### Task 2: Refactor TeamService

**Files:**
- Modify: `src/main/java/com/vvss/exam/service/TeamService.java`

- [ ] **Step 1: Update TeamService to use TeamDTO and throw ResourceNotFoundException**

```java
package com.vvss.exam.service;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<TeamDTO> findAll() {
        return teamRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO findById(Long id) {
        return teamRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    public TeamDTO save(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setCity(teamDTO.getCity());
        team.setName(teamDTO.getName());
        Team savedTeam = teamRepository.save(team);
        return mapToDTO(savedTeam);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    private TeamDTO mapToDTO(Team team) {
        return new TeamDTO(team.getId(), team.getCity(), team.getName());
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run: `mvn test -Dtest=TeamServiceTest`
Expected: PASS

- [ ] **Step 3: Add more tests to TeamServiceTest to ensure full coverage**

```java
    @Test
    void findAll_ShouldReturnListOfTeamDTOs() {
        com.vvss.exam.entity.Team team = new com.vvss.exam.entity.Team(1L, "New York", "Giants");
        when(teamRepository.findAll()).thenReturn(java.util.List.of(team));

        java.util.List<com.vvss.exam.dto.TeamDTO> result = teamService.findAll();

        org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
        org.junit.jupiter.api.Assertions.assertEquals("Giants", result.get(0).getName());
    }

    @Test
    void findById_WhenTeamExists_ShouldReturnTeamDTO() {
        Long teamId = 1L;
        com.vvss.exam.entity.Team team = new com.vvss.exam.entity.Team(teamId, "New York", "Giants");
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        com.vvss.exam.dto.TeamDTO result = teamService.findById(teamId);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals("Giants", result.getName());
    }

    @Test
    void save_ShouldReturnSavedTeamDTO() {
        com.vvss.exam.dto.TeamDTO teamDTO = new com.vvss.exam.dto.TeamDTO(null, "New York", "Giants");
        com.vvss.exam.entity.Team savedTeam = new com.vvss.exam.entity.Team(1L, "New York", "Giants");
        
        when(teamRepository.save(org.mockito.ArgumentMatchers.any(com.vvss.exam.entity.Team.class))).thenReturn(savedTeam);

        com.vvss.exam.dto.TeamDTO result = teamService.save(teamDTO);

        org.junit.jupiter.api.Assertions.assertNotNull(result.getId());
        org.junit.jupiter.api.Assertions.assertEquals("Giants", result.getName());
    }
```

- [ ] **Step 4: Run all TeamService tests**

Run: `mvn test -Dtest=TeamServiceTest`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/vvss/exam/service/TeamService.java src/test/java/com/vvss/exam/service/TeamServiceTest.java
git commit -m "refactor: update TeamService to use DTOs and ResourceNotFoundException"
```

---

### Task 3: Refactor TeamController

**Files:**
- Modify: `src/main/java/com/vvss/exam/controller/TeamController.java`

- [ ] **Step 1: Update TeamController to use TeamDTO**

```java
package com.vvss.exam.controller;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/list";
    }

    @GetMapping("/new")
    public String newTeamForm(Model model) {
        model.addAttribute("team", new TeamDTO());
        return "teams/form";
    }

    @PostMapping
    public String saveTeam(@ModelAttribute TeamDTO team) {
        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    public String editTeamForm(@PathVariable Long id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        return "teams/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
        return "redirect:/teams";
    }
}
```

- [ ] **Step 2: Run all tests to ensure no regressions**

Run: `mvn test`
Expected: PASS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/vvss/exam/controller/TeamController.java
git commit -m "refactor: update TeamController to use TeamDTO"
```
