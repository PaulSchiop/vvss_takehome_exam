package com.vvss.exam.service;

import com.vvss.exam.dto.TeamDTO;
import com.vvss.exam.entity.Team;
import com.vvss.exam.exception.ResourceNotFoundException;
import com.vvss.exam.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<TeamDTO> findAll() {
        return teamRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO findById(Long id) {
        return teamRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    public Team findEntityById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    public TeamDTO save(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setCity(teamDTO.getCity());
        team.setName(teamDTO.getName());
        Team savedTeam = teamRepository.save(team);
        return mapToDTO(savedTeam);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    private TeamDTO mapToDTO(Team team) {
        return new TeamDTO(team.getId(), team.getCity(), team.getName());
    }
}
