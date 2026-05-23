package com.vvss.exam.controller;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing team-related web requests.
 * Provides endpoints for listing, creating, editing, and deleting teams.
 */
@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    /**
     * Displays a list of all teams.
     *
     * @param model the Spring UI model
     * @return the name of the teams list view
     */
    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/list";
    }

    /**
     * Displays the form for creating a new team.
     *
     * @param model the Spring UI model
     * @return the name of the team form view
     */
    @GetMapping("/new")
    public String newTeamForm(Model model) {
        model.addAttribute("team", new TeamDTO());
        return "teams/form";
    }

    /**
     * Saves a new or updated team.
     *
     * @param team the team DTO from the form
     * @return a redirect to the teams list
     */
    @PostMapping
    public String saveTeam(@ModelAttribute TeamDTO team) {
        teamService.save(team);
        return "redirect:/teams";
    }

    /**
     * Displays the form for editing an existing team.
     *
     * @param id the unique identifier of the team to edit
     * @param model the Spring UI model
     * @return the name of the team form view
     */
    @GetMapping("/edit/{id}")
    public String editTeamForm(@PathVariable Long id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        return "teams/form";
    }

    /**
     * Deletes a team and redirects to the list.
     *
     * @param id the unique identifier of the team to delete
     * @return a redirect to the teams list
     */
    @GetMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
        return "redirect:/teams";
    }
}
