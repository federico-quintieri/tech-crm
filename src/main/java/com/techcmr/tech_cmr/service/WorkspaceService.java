package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.mapper.WorkspaceMapper;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceMapper workspaceMapper;

    // READ
    public List<WorkspaceDTO> findAllWorkspaces() {
        return workspaceRepository.findAll().stream().map(workspaceMapper::toDto).toList();
    }

    // SHOW
    public WorkspaceDTO findWorkspaceById(Long id) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workspace not found"));
        return workspaceMapper.toDto(workspace);
    }

    // CREATE
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceMapper.toDto(savedWorkspace);
    }

    // UPDATE
    public void updateWorkspace(Long id, WorkspaceDTO workspaceDTO) {
        // Trovo dal db workspace in base ad id
        Workspace updatedWorkspace = workspaceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workspace not found"));
        // lo mergio con il dto del body
        workspaceMapper.updateEntityFromDto(workspaceDTO, updatedWorkspace);
        // salvo l'entity mergiata
        workspaceRepository.save(updatedWorkspace);
    }

    // DELETE
    public void deleteWorkspace(Long id) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workspace not found"));
        workspaceRepository.delete(workspace);
    }
}
