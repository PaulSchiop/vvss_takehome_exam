# Player Layer Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `PlayerService` and `PlayerController` to use `PlayerDTO` and handle `ResourceNotFoundException`.

**Architecture:** Advanced MVC with DTOs and custom exception handling. The service layer will handle mapping between Entities and DTOs and throw business exceptions.

**Tech Stack:** Java, Spring Boot, JUnit 5, Mockito, Lombok.

---

### Task 1: Setup Failing Test for PlayerService

**Files:**
- Create: `src/test/java/com/vvss/exam/service/PlayerServiceTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.vvss.exam.service;

import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void findById_WhenPlayerNotFound_ShouldThrowResourceNotFoundException() {
        Long playerId = 1L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.findById(playerId));
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -Dtest=PlayerServiceTest`
Expected: FAIL (because `PlayerService.findById` returns `Optional<Player>` and doesn't throw `ResourceNotFoundException`)

- [ ] **Step 3: Commit**

```bash
git add src/test/java/com/vvss/exam/service/PlayerServiceTest.java
git commit -m "test: add failing test for PlayerService findById not found"
```

### Task 2: Refactor PlayerService to use DTOs and Exceptions

**Files:**
- Modify: `src/main/java/com/vvss/exam/service/PlayerService.java`

- [ ] **Step 1: Implement DTO mapping and Exception throwing**

```java
package com.vvss.exam.service;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.entity.Player;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public List<PlayerDTO> findAll() {
        return playerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO findById(Long id) {
        return playerRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    public PlayerDTO save(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setPosition(playerDTO.getPosition());
        
        Player savedPlayer = playerRepository.save(player);
        return mapToDTO(savedPlayer);
    }

    public void deleteById(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player not found with id: " + id);
        }
        playerRepository.deleteById(id);
    }

    private PlayerDTO mapToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getPosition());
    }
}
```

- [ ] **Step 2: Run tests to verify they pass**

Run: `mvn test -Dtest=PlayerServiceTest`
Expected: PASS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/vvss/exam/service/PlayerService.java
git commit -m "refactor: update PlayerService to use PlayerDTO and ResourceNotFoundException"
```

### Task 3: Refactor PlayerController to use PlayerDTO

**Files:**
- Modify: `src/main/java/com/vvss/exam/controller/PlayerController.java`

- [ ] **Step 1: Update controller methods**

```java
package com.vvss.exam.controller;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.findAll());
        return "players/list";
    }

    @GetMapping("/new")
    public String newPlayerForm(Model model) {
        model.addAttribute("player", new PlayerDTO());
        return "players/form";
    }

    @PostMapping
    public String savePlayer(@ModelAttribute PlayerDTO player) {
        playerService.save(player);
        return "redirect:/players";
    }

    @GetMapping("/edit/{id}")
    public String editPlayerForm(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.findById(id));
        return "players/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/players";
    }
}
```

- [ ] **Step 2: Run all tests**

Run: `mvn test`
Expected: PASS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/vvss/exam/controller/PlayerController.java
git commit -m "refactor: update PlayerController to use PlayerDTO"
```
