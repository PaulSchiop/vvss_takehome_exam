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
