package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.mapper.TeamMapper;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMapper teamMapper;

    // READ
    public List<TeamDTO> findAllTeams() {
        return teamRepository.findAll().stream().map(teamMapper::toDto).toList();
    }

    // SHOW
    public TeamDTO findTeamById(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        return teamMapper.toDto(team);
    }

    // CREATE
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        Team savedTeam = teamRepository.save(team);
        return teamMapper.toDto(savedTeam);
    }

    // UPDATE
    public void updateTeam(Long id, TeamDTO teamDTO) {
        Team updatedTeam = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamMapper.updateEntityFromDto(teamDTO, updatedTeam);
        teamRepository.save(updatedTeam);
    }

    // DELETE
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamRepository.delete(team);
    }

}
