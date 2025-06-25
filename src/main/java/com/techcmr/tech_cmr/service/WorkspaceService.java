package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.mapper.WorkspaceMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMapper workspaceMapper;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository,
                          TeamRepository teamRepository,
                          ProjectRepository projectRepository,
                          WorkspaceMapper workspaceMapper) {
        this.workspaceRepository = workspaceRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
        this.workspaceMapper = workspaceMapper;
    }

    public Optional<Workspace> findById(Long id) {
        return workspaceRepository.findById(id);
    }

    public List<WorkspaceDTO> findAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .toList();
    }

    public WorkspaceDTO findWorkspaceById(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + id));
        return workspaceMapper.toDto(workspace);
    }

    @Transactional
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO, teamRepository, projectRepository);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        
        // Aggiorna le relazioni bidirezionali dopo il salvataggio
        updateRelationships(savedWorkspace);
        
        return workspaceMapper.toDto(savedWorkspace);
    }

    @Transactional
    public WorkspaceDTO updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + id));

        // 1. Salva copie delle relazioni esistenti
        List<Team> currentTeams = new ArrayList<>(existingWorkspace.getTeams());
        List<Project> currentProjects = new ArrayList<>(existingWorkspace.getProjects());

        // 2. Scollega tutte le relazioni esistenti
        existingWorkspace.getTeams().clear();
        existingWorkspace.getProjects().clear();
        workspaceRepository.save(existingWorkspace); // Salva senza flush per evitare problemi

        // 3. Applica le modifiche dal DTO
        workspaceMapper.updateEntityFromDto(workspaceDTO, existingWorkspace, teamRepository, projectRepository);

        // 4. Gestisci le relazioni in modo transazionale
        updateTeamRelationships(existingWorkspace, currentTeams);
        updateProjectRelationships(existingWorkspace, currentProjects);

        // 5. Salva il workspace con le nuove relazioni
        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);
        return workspaceMapper.toDto(updatedWorkspace);
    }

    @Transactional
    public void deleteWorkspace(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + id));

        // Scollega tutte le relazioni prima di eliminare
        if (!workspace.getTeams().isEmpty()) {
            workspace.getTeams().forEach(team -> team.setWorkspace(null));
            teamRepository.saveAll(workspace.getTeams());
        }
        if (!workspace.getProjects().isEmpty()) {
            workspace.getProjects().forEach(project -> project.setWorkspace(null));
            projectRepository.saveAll(workspace.getProjects());
        }

        workspaceRepository.delete(workspace);
    }

    // ===== Private Helper Methods =====

    private void updateRelationships(Workspace workspace) {
        // Aggiorna i team
        if (!workspace.getTeams().isEmpty()) {
            workspace.getTeams().forEach(team -> {
                team.setWorkspace(workspace);
                teamRepository.save(team);
            });
        }

        // Aggiorna i progetti
        if (!workspace.getProjects().isEmpty()) {
            workspace.getProjects().forEach(project -> {
                project.setWorkspace(workspace);
                projectRepository.save(project);
            });
        }
    }

    private void updateTeamRelationships(Workspace workspace, List<Team> oldTeams) {
        // Scollega team non più presenti
        oldTeams.stream()
            .filter(oldTeam -> !workspace.getTeams().contains(oldTeam))
            .forEach(team -> {
                team.setWorkspace(null);
                teamRepository.save(team);
            });

        // Collega i nuovi team
        workspace.getTeams().forEach(team -> {
            if (team.getWorkspace() == null || !team.getWorkspace().equals(workspace)) {
                team.setWorkspace(workspace);
                teamRepository.save(team);
            }
        });
    }

    private void updateProjectRelationships(Workspace workspace, List<Project> oldProjects) {
        // Scollega progetti non più presenti
        oldProjects.stream()
            .filter(oldProject -> !workspace.getProjects().contains(oldProject))
            .forEach(project -> {
                project.setWorkspace(null);
                projectRepository.save(project);
            });

        // Collega i nuovi progetti
        workspace.getProjects().forEach(project -> {
            if (project.getWorkspace() == null || !project.getWorkspace().equals(workspace)) {
                project.setWorkspace(workspace);
                projectRepository.save(project);
            }
        });
    }
}