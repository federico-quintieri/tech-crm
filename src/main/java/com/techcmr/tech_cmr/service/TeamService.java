package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.mapper.TeamMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.relations.TeamRelationManager;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private TeamRelationManager teamRelationManager;

    @Autowired
    private TeamMapper teamMapper;

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

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
        // 1
        Team team = teamMapper.toEntity(teamDTO, projectRepository, workspaceRepository);

        // 2
        teamRelationManager.updateRelationsForPostTeam(team);

        // 3
        Team savedTeam = teamRepository.save(team);

        // 4
        return teamMapper.toDto(savedTeam);
    }

    // UPDATE
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        // 1
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        // 2
        teamMapper.updateEntityFromDto(teamDTO, existingTeam, projectRepository, workspaceRepository);

        // 3
        teamRelationManager.updateProjectsForTeam(existingTeam, teamDTO);

        // 4. Finalmente salva il team
        Team updatedTeam = teamRepository.save(existingTeam);

        // 5
        return teamMapper.toDto(updatedTeam);
    }

    // DELETE
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamRepository.delete(team);
    }

}
