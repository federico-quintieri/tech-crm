package com.techcmr.tech_cmr.controller;

import org.springframework.web.bind.annotation.RestController;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.service.WorkspaceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    // GET /workspaces - Recupera tutti i workspace
    @GetMapping
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces() {
        List<WorkspaceDTO> workspaces = workspaceService.findAllWorkspaces();
        return ResponseEntity.ok(workspaces);
    }

    // GET /workspaces/{id} - Recupera un workspace specifico
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        try {
            WorkspaceDTO workspace = workspaceService.findWorkspaceById(id);
            return ResponseEntity.ok(workspace);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /workspaces - Crea un nuovo workspace
    @PostMapping
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        WorkspaceDTO created = workspaceService.createWorkspace(workspaceDTO);
        return ResponseEntity.ok(created);
    }

    // PUT /workspaces/{id} - Aggiorna un workspace
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkspace(@PathVariable Long id, @RequestBody WorkspaceDTO workspaceDTO) {
        try {
            workspaceService.updateWorkspace(id, workspaceDTO);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /workspaces/{id} - Elimina un workspace
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        try {
            workspaceService.deleteWorkspace(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}