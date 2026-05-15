package com.vvss.exam.controller;

import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Team;
import com.vvss.exam.service.ContractService;
import com.vvss.exam.service.PlayerService;
import com.vvss.exam.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final TeamService teamService;
    private final PlayerService playerService;

    @GetMapping("/sign")
    public String signFreeAgentForm(Model model) {
        model.addAttribute("contract", new Contract());
        model.addAttribute("players", playerService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "contracts/sign";
    }

    @PostMapping("/sign")
    public String signFreeAgent(@Valid @ModelAttribute Contract contract, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("players", playerService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "contracts/sign";
        }
        contractService.save(contract);
        return "redirect:/contracts/report";
    }

    @GetMapping("/report")
    public String reportForm(Model model, @RequestParam(required = false) Long teamId) {
        model.addAttribute("teams", teamService.findAll());
        if (teamId != null) {
            Team team = teamService.findById(teamId).orElse(null);
            if (team != null) {
                List<Contract> contracts = contractService.findByTeamId(teamId);
                Integer totalPayroll = contractService.calculateTeamPayroll(teamId);
                model.addAttribute("selectedTeam", team);
                model.addAttribute("contracts", contracts);
                model.addAttribute("totalPayroll", totalPayroll);
            }
        }
        return "contracts/report";
    }
}
