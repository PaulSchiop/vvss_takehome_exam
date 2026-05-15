package com.vvss.exam.controller;

import com.vvss.exam.entity.Team;
import com.vvss.exam.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/list";
    }

    @GetMapping("/new")
    public String newTeamForm(Model model) {
        model.addAttribute("team", new Team());
        return "teams/form";
    }

    @PostMapping
    public String saveTeam(@ModelAttribute Team team) {
        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    public String editTeamForm(@PathVariable Long id, Model model) {
        model.addAttribute("team", teamService.findById(id).orElseThrow());
        return "teams/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
        return "redirect:/teams";
    }
}
