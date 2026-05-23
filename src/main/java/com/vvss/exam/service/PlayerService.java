package com.vvss.exam.service;

import com.vvss.exam.dto.PlayerDTO;
import com.vvss.exam.entity.Player;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public List<PlayerDTO> findAll() {
        return playerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO findById(Long id) {
        return playerRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    public Player findEntityById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    public PlayerDTO save(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setPosition(playerDTO.getPosition());
        
        Player savedPlayer = playerRepository.save(player);
        return mapToDTO(savedPlayer);
    }

    public void deleteById(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player not found with id: " + id);
        }
        playerRepository.deleteById(id);
    }

    private PlayerDTO mapToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getPosition());
    }
}
