package com.vvss.exam.service;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing teams.
 * Handles business logic related to team operations.
 */
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    /**
     * Retrieves all teams from the database.
     *
     * @return a list of all teams as DTOs
     */
    public List<TeamDTO> findAll() {
        return teamRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a team by its unique identifier.
     *
     * @param id the unique identifier of the team
     * @return the team DTO if found
     * @throws ResourceNotFoundException if no team is found with the given id
     */
    public TeamDTO findById(Long id) {
        return teamRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    /**
     * Finds a team entity by its unique identifier.
     *
     * @param id the unique identifier of the team
     * @return the team entity if found
     * @throws ResourceNotFoundException if no team is found with the given id
     */
    public Team findEntityById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    /**
     * Saves a team to the database.
     *
     * @param teamDTO the team data to be saved
     * @return the saved team as a DTO
     */
    public TeamDTO save(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setCity(teamDTO.getCity());
        team.setName(teamDTO.getName());
        Team savedTeam = teamRepository.save(team);
        return mapToDTO(savedTeam);
    }

    /**
     * Deletes a team by its unique identifier.
     *
     * @param id the unique identifier of the team to delete
     */
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    /**
     * Maps a Team entity to a TeamDTO.
     *
     * @param team the team entity to map
     * @return the mapped team DTO
     */
    private TeamDTO mapToDTO(Team team) {
        return new TeamDTO(team.getId(), team.getCity(), team.getName());
    }
}
