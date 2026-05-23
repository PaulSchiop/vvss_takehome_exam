package com.vvss.exam.controller;

import com.vvss.exam.dto.ContractRequestDTO;
import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.dto.TeamReportDTO;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.exception.ResourceNotFoundException;
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
        model.addAttribute("contractRequest", new ContractRequestDTO());
        model.addAttribute("players", playerService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "contracts/sign";
    }

    @PostMapping("/sign")
    public String signFreeAgent(@Valid @ModelAttribute("contractRequest") ContractRequestDTO request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("players", playerService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "contracts/sign";
        }
        contractService.signFreeAgent(request);
        return "redirect:/contracts/report";
    }

    @GetMapping("/report")
    public String reportForm(Model model, @RequestParam(required = false) Long teamId) {
        model.addAttribute("teams", teamService.findAll());
        if (teamId != null) {
            try {
                TeamReportDTO report = contractService.getTeamPayrollReport(teamId);
                model.addAttribute("report", report);
            } catch (ResourceNotFoundException e) {
                // Team not found, just don't add to model
            }
        }
        return "contracts/report";
    }
}
