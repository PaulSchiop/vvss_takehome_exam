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

/**
 * Controller for managing contract-related web requests.
 * Provides endpoints for signing players and viewing payroll reports.
 */
@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final TeamService teamService;
    private final PlayerService playerService;

    /**
     * Displays the form for signing a free agent player to a team.
     *
     * @param model the Spring UI model
     * @return the name of the contract signing view
     */
    @GetMapping("/sign")
    public String signFreeAgentForm(Model model) {
        model.addAttribute("contractRequest", new ContractRequestDTO());
        model.addAttribute("players", playerService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "contracts/sign";
    }

    /**
     * Processes the signing of a free agent.
     *
     * @param request the contract request data
     * @param bindingResult the result of data validation
     * @param model the Spring UI model
     * @return a redirect to the payroll report or the signing form if there are errors
     */
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

    /**
     * Displays the payroll report for a specific team or the selection form.
     *
     * @param model the Spring UI model
     * @param teamId the unique identifier of the team (optional)
     * @return the name of the payroll report view
     */
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
