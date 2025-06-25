package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.mapper.TeamMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
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
        Team team = teamMapper.toEntity(teamDTO, projectRepository, workspaceRepository);
        Team savedTeam = teamRepository.save(team);

        // Aggiorna la relazione bidirezionale
        if (savedTeam.getProjects() != null) {
            savedTeam.getProjects().forEach(project -> {
                project.setTeam(savedTeam);
                projectRepository.save(project);
            });
        }

        return teamMapper.toDto(savedTeam);
    }

    // UPDATE
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        // 1. Ottieni i progetti attuali prima di qualsiasi modifica
        List<Project> currentProjects = existingTeam.getProjects() != null ? new ArrayList<>(existingTeam.getProjects())
                : new ArrayList<>();

        // 2. Aggiorna il team con i nuovi dati MA non salvare ancora
        teamMapper.updateEntityFromDto(teamDTO, existingTeam, projectRepository, workspaceRepository);

        // 3. Gestisci le relazioni in modo transazionale
        if (existingTeam.getProjects() != null) {
            // Rimuovi i progetti non più presenti
            currentProjects.stream()
                    .filter(p -> !existingTeam.getProjects().contains(p))
                    .forEach(p -> {
                        p.setTeam(null);
                        projectRepository.save(p);
                    });

            // Aggiungi i nuovi progetti
            existingTeam.getProjects().forEach(p -> {
                p.setTeam(existingTeam);
                projectRepository.save(p);
            });
        }

        // 4. Finalmente salva il team
        Team updatedTeam = teamRepository.save(existingTeam);
        return teamMapper.toDto(updatedTeam);
    }

    // DELETE
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamRepository.delete(team);
    }

}
