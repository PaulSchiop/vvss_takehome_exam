package com.vvss.exam.service;

import com.vvss.exam.entity.Player;
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

    @Test
    void findAll_ShouldReturnListOfPlayerDTOs() {
        Player player = new Player(1L, "John Doe", "Forward");
        when(playerRepository.findAll()).thenReturn(java.util.List.of(player));

        java.util.List<com.vvss.exam.dto.PlayerDTO> result = playerService.findAll();

        org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
        org.junit.jupiter.api.Assertions.assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void findById_WhenPlayerExists_ShouldReturnPlayerDTO() {
        Long playerId = 1L;
        Player player = new Player(playerId, "John Doe", "Forward");
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        com.vvss.exam.dto.PlayerDTO result = playerService.findById(playerId);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals("John Doe", result.getName());
    }

    @Test
    void save_ShouldReturnSavedPlayerDTO() {
        com.vvss.exam.dto.PlayerDTO playerDTO = new com.vvss.exam.dto.PlayerDTO(null, "John Doe", "Forward");
        Player player = new Player(null, "John Doe", "Forward");
        Player savedPlayer = new Player(1L, "John Doe", "Forward");
        
        when(playerRepository.save(org.mockito.ArgumentMatchers.any(Player.class))).thenReturn(savedPlayer);

        com.vvss.exam.dto.PlayerDTO result = playerService.save(playerDTO);

        org.junit.jupiter.api.Assertions.assertNotNull(result.getId());
        org.junit.jupiter.api.Assertions.assertEquals("John Doe", result.getName());
    }
}
