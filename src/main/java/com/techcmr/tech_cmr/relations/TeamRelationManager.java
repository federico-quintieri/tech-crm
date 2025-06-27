package com.techcmr.tech_cmr.relations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.repository.ProjectRepository;

@Component
public class TeamRelationManager {

    private final ProjectRepository projectRepository;

    public TeamRelationManager(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Metodo che aggiorna relazione Projects a Team
    public void updateProjectsForTeam(Team team, TeamDTO dto) {
        List<Project> existingProjects = new ArrayList<>(team.getProjects());
        List<Project> updatedProjects = new ArrayList<>();

        if (dto.getProjectIds() != null) {
            for (Long projectId : dto.getProjectIds()) {
                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
                project.setTeam(team);
                updatedProjects.add(project);
            }
        }
        for (Project oldProject : existingProjects) {
            if (!updatedProjects.contains(oldProject)) {
                oldProject.setTeam(null);
            }
        }
    }

    // Metodo che aggiorna le relazioni a chiamata POST
    public void updateRelationsForPostTeam(Team team) {
        if (team.getProjects() != null) {
            for (Project project : team.getProjects()) {
                project.setTeam(team);
            }
        }
    }

}
