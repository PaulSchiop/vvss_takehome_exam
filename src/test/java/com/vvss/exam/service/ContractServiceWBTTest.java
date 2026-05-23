package com.vvss.exam.service;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.dto.TeamReportDTO;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceWBTTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private ContractService contractService;

    private Long teamId;
    private TeamDTO teamDTO;
    private Team teamEntity;

    @BeforeEach
    void setUp() {
        teamId = 1L;
        teamDTO = new TeamDTO(teamId, "Los Angeles", "Lakers");
        teamEntity = new Team(teamId, "Los Angeles", "Lakers");
    }

    /**
     * Path 1: Team Not Found
     * CFG: A -> B -> Branch 1-F -> Exception
     */
    @Test
    void getTeamPayrollReport_TeamNotFound_ThrowsException() {
        // Arrange
        when(teamService.findById(teamId)).thenThrow(new ResourceNotFoundException("Team not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> contractService.getTeamPayrollReport(teamId));
        verify(teamService).findById(teamId);
        verifyNoInteractions(contractRepository);
    }

    /**
     * Path 2: Team Exists, Zero Contracts
     * CFG: A -> B -> Branch 1-T -> C -> D -> E
     */
    @Test
    void getTeamPayrollReport_ZeroContracts_ReturnsEmptyReport() {
        // Arrange
        when(teamService.findById(teamId)).thenReturn(teamDTO);
        when(contractRepository.findByTeamId(teamId)).thenReturn(Collections.emptyList());

        // Act
        TeamReportDTO report = contractService.getTeamPayrollReport(teamId);

        // Assert
        assertNotNull(report);
        assertEquals("Lakers", report.getTeamName());
        assertTrue(report.getContracts().isEmpty());
        assertEquals(0, report.getTotalPayroll());

        verify(teamService).findById(teamId);
        verify(contractRepository).findByTeamId(teamId);
    }

    /**
     * Path 3: Team Exists, Multiple Contracts
     * CFG: A -> B -> Branch 1-T -> C -> D -> E
     */
    @Test
    void getTeamPayrollReport_MultipleContracts_ReturnsCalculatedReport() {
        // Arrange
        Player p1 = new Player(1L, "LeBron James", "Forward");
        Player p2 = new Player(2L, "Anthony Davis", "Center");
        Contract c1 = new Contract(1L, 1000, p1, teamEntity);
        Contract c2 = new Contract(2L, 2000, p2, teamEntity);
        List<Contract> contracts = Arrays.asList(c1, c2);

        when(teamService.findById(teamId)).thenReturn(teamDTO);
        when(contractRepository.findByTeamId(teamId)).thenReturn(contracts);

        // Act
        TeamReportDTO report = contractService.getTeamPayrollReport(teamId);

        // Assert
        assertNotNull(report);
        assertEquals("Lakers", report.getTeamName());
        assertEquals(2, report.getContracts().size());
        assertEquals(3000, report.getTotalPayroll());
        
        assertEquals(1000, report.getContracts().get(0).getSalary());
        assertEquals("LeBron James", report.getContracts().get(0).getPlayerDTO().getName());
        assertEquals(2000, report.getContracts().get(1).getSalary());
        assertEquals("Anthony Davis", report.getContracts().get(1).getPlayerDTO().getName());

        verify(teamService).findById(teamId);
        verify(contractRepository).findByTeamId(teamId);
    }
}
