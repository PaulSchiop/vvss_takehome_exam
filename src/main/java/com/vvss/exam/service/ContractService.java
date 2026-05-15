package com.vvss.exam.service;

import com.vvss.exam.entity.Contract;
import com.vvss.exam.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;

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
}
