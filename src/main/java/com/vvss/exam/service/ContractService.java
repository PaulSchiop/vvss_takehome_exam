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

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final PlayerService playerService;
    private final TeamService teamService;

    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    public List<Contract> findByTeamId(Long teamId) {
        return contractRepository.findByTeamId(teamId);
    }
    
    public Integer calculateTeamPayroll(Long teamId) {
        return contractRepository.findByTeamId(teamId).stream()
                .map(Contract::getSalary)
                .reduce(0, Integer::sum);
    }

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

    private ContractDTO mapToDTO(Contract contract) {
        PlayerDTO playerDTO = new PlayerDTO(contract.getPlayer().getId(), contract.getPlayer().getName(), contract.getPlayer().getPosition());
        TeamDTO teamDTO = new TeamDTO(contract.getTeam().getId(), contract.getTeam().getCity(), contract.getTeam().getName());
        return new ContractDTO(contract.getId(), contract.getSalary(), playerDTO, teamDTO);
    }
}
