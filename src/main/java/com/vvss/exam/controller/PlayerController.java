package com.vvss.exam.controller;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.findAll());
        return "players/list";
    }

    @GetMapping("/new")
    public String newPlayerForm(Model model) {
        model.addAttribute("player", new PlayerDTO());
        return "players/form";
    }

    @PostMapping
    public String savePlayer(@ModelAttribute PlayerDTO player) {
        playerService.save(player);
        return "redirect:/players";
    }

    @GetMapping("/edit/{id}")
    public String editPlayerForm(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.findById(id));
        return "players/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/players";
    }
}
