package com.vvss.exam.service;

import com.vvss.exam.dto.ContractDTO;
import com.vvss.exam.dto.ContractRequestDTO;
import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.dto.TeamReportDTO;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing player contracts.
 * Handles business logic related to contract signing and payroll reporting.
 */
@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final PlayerService playerService;
    private final TeamService teamService;

    /**
     * Saves a contract to the database.
     *
     * @param contract the contract entity to save
     * @return the saved contract entity
     */
    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    /**
     * Finds all contracts associated with a specific team.
     *
     * @param teamId the unique identifier of the team
     * @return a list of contracts for the team
     */
    public List<Contract> findByTeamId(Long teamId) {
        return contractRepository.findByTeamId(teamId);
    }
    
    /**
     * Calculates the total payroll for a specific team.
     *
     * @param teamId the unique identifier of the team
     * @return the total payroll amount
     */
    public Integer calculateTeamPayroll(Long teamId) {
        return contractRepository.findByTeamId(teamId).stream()
                .map(Contract::getSalary)
                .reduce(0, Integer::sum);
    }

    /**
     * Generates a payroll report for a specific team.
     *
     * @param teamId the unique identifier of the team
     * @return a DTO containing team details, all contracts, and total payroll
     * @throws com.vvss.exam.exception.ResourceNotFoundException if the team is not found
     */
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

    /**
     * Signs a free agent player to a team.
     *
     * @param request the contract request details (player ID, team ID, salary)
     * @return the created contract as a DTO
     * @throws com.vvss.exam.exception.ResourceNotFoundException if player or team is not found
     */
    public ContractDTO signFreeAgent(ContractRequestDTO request) {
        if (request.getSalary() == null || request.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }

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

    /**
     * Maps a Contract entity to a ContractDTO.
     *
     * @param contract the contract entity to map
     * @return the mapped contract DTO
     */
    private ContractDTO mapToDTO(Contract contract) {
        PlayerDTO playerDTO = new PlayerDTO(contract.getPlayer().getId(), contract.getPlayer().getName(), contract.getPlayer().getPosition());
        TeamDTO teamDTO = new TeamDTO(contract.getTeam().getId(), contract.getTeam().getCity(), contract.getTeam().getName());
        return new ContractDTO(contract.getId(), contract.getSalary(), playerDTO, teamDTO);
    }
}
