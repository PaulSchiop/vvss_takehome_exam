package com.vvss.exam.service;

import com.vvss.exam.dto.ContractDTO;
import com.vvss.exam.dto.ContractRequestDTO;
import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceBBTTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private ContractService contractService;

    private Player player;
    private Team team;

    @BeforeEach
    void setUp() {
        player = new Player(1L, "Test Player", "Forward");
        team = new Team(1L, "Test City", "Test Team");
    }

    @Test
    @DisplayName("TC1: Valid case - All inputs are valid and exist")
    void signFreeAgent_ValidCase() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, 50000);
        Contract savedContract = new Contract(1L, 50000, player, team);

        when(playerService.findById(1L)).thenReturn(new PlayerDTO());
        when(teamService.findById(1L)).thenReturn(new TeamDTO());
        when(playerService.findEntityById(1L)).thenReturn(player);
        when(teamService.findEntityById(1L)).thenReturn(team);
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);

        ContractDTO result = contractService.signFreeAgent(request);

        assertNotNull(result);
        assertEquals(50000, result.getSalary());
        assertEquals(1L, result.getPlayerDTO().getId());
        assertEquals(1L, result.getTeamDTO().getId());
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    @DisplayName("TC2: Player Invalid - playerId does not exist")
    void signFreeAgent_PlayerNotFound() {
        ContractRequestDTO request = new ContractRequestDTO(99L, 1L, 50000);
        
        when(playerService.findById(99L)).thenThrow(new ResourceNotFoundException("Player not found"));

        assertThrows(ResourceNotFoundException.class, () -> contractService.signFreeAgent(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("TC3: Team Invalid - teamId does not exist")
    void signFreeAgent_TeamNotFound() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 99L, 50000);
        
        when(playerService.findById(1L)).thenReturn(new PlayerDTO());
        when(teamService.findById(99L)).thenThrow(new ResourceNotFoundException("Team not found"));

        assertThrows(ResourceNotFoundException.class, () -> contractService.signFreeAgent(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("TC4: Salary Boundary - salary = 1 (valid min)")
    void signFreeAgent_SalaryMinValid() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, 1);
        Contract savedContract = new Contract(1L, 1, player, team);

        when(playerService.findById(1L)).thenReturn(new PlayerDTO());
        when(teamService.findById(1L)).thenReturn(new TeamDTO());
        when(playerService.findEntityById(1L)).thenReturn(player);
        when(teamService.findEntityById(1L)).thenReturn(team);
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);

        ContractDTO result = contractService.signFreeAgent(request);

        assertNotNull(result);
        assertEquals(1, result.getSalary());
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    @DisplayName("BVA: Salary = Integer.MAX_VALUE (valid max)")
    void signFreeAgent_SalaryMaxValid() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, Integer.MAX_VALUE);
        Contract savedContract = new Contract(1L, Integer.MAX_VALUE, player, team);

        when(playerService.findById(1L)).thenReturn(new PlayerDTO());
        when(teamService.findById(1L)).thenReturn(new TeamDTO());
        when(playerService.findEntityById(1L)).thenReturn(player);
        when(teamService.findEntityById(1L)).thenReturn(team);
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);

        ContractDTO result = contractService.signFreeAgent(request);

        assertNotNull(result);
        assertEquals(Integer.MAX_VALUE, result.getSalary());
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    @DisplayName("TC5: Salary Boundary - salary = 0 (invalid)")
    void signFreeAgent_SalaryZero() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, 0);
        assertThrows(IllegalArgumentException.class, () -> contractService.signFreeAgent(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("TC6: Salary Boundary - salary = -1 (invalid)")
    void signFreeAgent_SalaryNegative() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, -1);
        assertThrows(IllegalArgumentException.class, () -> contractService.signFreeAgent(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    @DisplayName("EC3: Player ID null")
    void signFreeAgent_PlayerIdNull() {
        ContractRequestDTO request = new ContractRequestDTO(null, 1L, 50000);
        // Service should probably throw before saving
        assertThrows(Exception.class, () -> contractService.signFreeAgent(request));
    }

    @Test
    @DisplayName("EC6: Team ID null")
    void signFreeAgent_TeamIdNull() {
        ContractRequestDTO request = new ContractRequestDTO(1L, null, 50000);
        assertThrows(Exception.class, () -> contractService.signFreeAgent(request));
    }

    @Test
    @DisplayName("EC10: Salary null")
    void signFreeAgent_SalaryNull() {
        ContractRequestDTO request = new ContractRequestDTO(1L, 1L, null);
        assertThrows(IllegalArgumentException.class, () -> contractService.signFreeAgent(request));
        verify(contractRepository, never()).save(any(Contract.class));
    }
}
