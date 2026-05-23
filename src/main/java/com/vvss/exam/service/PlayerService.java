package com.vvss.exam.service;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.entity.Player;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing players.
 * Handles business logic related to player operations.
 */
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    /**
     * Retrieves all players from the database.
     *
     * @return a list of all players as DTOs
     */
    public List<PlayerDTO> findAll() {
        return playerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a player by their unique identifier.
     *
     * @param id the unique identifier of the player
     * @return the player DTO if found
     * @throws ResourceNotFoundException if no player is found with the given id
     */
    public PlayerDTO findById(Long id) {
        return playerRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    /**
     * Finds a player entity by its unique identifier.
     *
     * @param id the unique identifier of the player
     * @return the player entity if found
     * @throws ResourceNotFoundException if no player is found with the given id
     */
    public Player findEntityById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    /**
     * Saves a player to the database.
     *
     * @param playerDTO the player data to be saved
     * @return the saved player as a DTO
     */
    public PlayerDTO save(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setPosition(playerDTO.getPosition());
        
        Player savedPlayer = playerRepository.save(player);
        return mapToDTO(savedPlayer);
    }

    /**
     * Deletes a player by their unique identifier.
     *
     * @param id the unique identifier of the player to delete
     * @throws ResourceNotFoundException if no player is found with the given id
     */
    public void deleteById(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player not found with id: " + id);
        }
        playerRepository.deleteById(id);
    }

    /**
     * Maps a Player entity to a PlayerDTO.
     *
     * @param player the player entity to map
     * @return the mapped player DTO
     */
    private PlayerDTO mapToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getPosition());
    }
}
