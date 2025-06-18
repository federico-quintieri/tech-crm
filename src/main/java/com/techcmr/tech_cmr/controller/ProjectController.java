package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // READ all
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> findAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }

    // READ 1
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        // Setto id nell'oggetto body
        projectDTO.setId(id);
        projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok("Project updated successfully");
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted");
    }

}
