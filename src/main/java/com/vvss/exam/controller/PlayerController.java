package com.vvss.exam.controller;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing player-related web requests.
 * Provides endpoints for listing, creating, editing, and deleting players.
 */
@Controller
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    /**
     * Displays a list of all players.
     *
     * @param model the Spring UI model
     * @return the name of the players list view
     */
    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.findAll());
        return "players/list";
    }

    /**
     * Displays the form for creating a new player.
     *
     * @param model the Spring UI model
     * @return the name of the player form view
     */
    @GetMapping("/new")
    public String newPlayerForm(Model model) {
        model.addAttribute("player", new PlayerDTO());
        return "players/form";
    }

    /**
     * Saves a new or updated player.
     *
     * @param player the player DTO from the form
     * @return a redirect to the players list
     */
    @PostMapping
    public String savePlayer(@ModelAttribute PlayerDTO player) {
        playerService.save(player);
        return "redirect:/players";
    }

    /**
     * Displays the form for editing an existing player.
     *
     * @param id the unique identifier of the player to edit
     * @param model the Spring UI model
     * @return the name of the player form view
     */
    @GetMapping("/edit/{id}")
    public String editPlayerForm(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.findById(id));
        return "players/form";
    }

    /**
     * Deletes a player and redirects to the list.
     *
     * @param id the unique identifier of the player to delete
     * @return a redirect to the players list
     */
    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/players";
    }
}
