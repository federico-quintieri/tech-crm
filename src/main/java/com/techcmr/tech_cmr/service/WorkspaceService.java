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

    // CREATE
    @Transactional
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO, teamRepository, projectRepository);

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return workspaceMapper.toDto(savedWorkspace);
    }

    // UDPATE
    @Transactional
    public WorkspaceDTO updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + id));

        // 3. Applica le modifiche dal DTO
        workspaceMapper.updateEntityFromDto(workspaceDTO, existingWorkspace, teamRepository, projectRepository);

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

}