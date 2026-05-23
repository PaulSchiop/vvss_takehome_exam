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
}
