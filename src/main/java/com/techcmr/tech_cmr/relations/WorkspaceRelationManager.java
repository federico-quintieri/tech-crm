package com.techcmr.tech_cmr.relations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;

@Component
public class WorkspaceRelationManager {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    // Costruttore
    public WorkspaceRelationManager(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
    }

    // Metodo che aggiorna relazione Progetti a workspace
    public void updateProjectsForWorkspace(Workspace workspace, WorkspaceDTO dto) {
        List<Project> existingProjects = new ArrayList<>(workspace.getProjects());
        List<Project> updatedProjects = new ArrayList<>();

        if (dto.getProjectIds() != null) {
            for (Long projectId : dto.getProjectIds()) {
                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
                project.setWorkspace(workspace);
                updatedProjects.add(project);
            }
        }

        for (Project oldProject : existingProjects) {
            if (!updatedProjects.contains(oldProject)) {
                oldProject.setWorkspace(null);
            }
        }
        workspace.setProjects(updatedProjects);
    }

    // Metodo che aggiorna relazione Teams a workspace
    public void updateTeamsForWorkspace(Workspace workspace, WorkspaceDTO dto) {
        List<Team> existingTeams = new ArrayList<>(workspace.getTeams());
        List<Team> updatedTeams = new ArrayList<>();

        if (dto.getTeamIds() != null) {
            for (Long teamId : dto.getTeamIds()) {
                Team team = teamRepository.findById(teamId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
                team.setWorkspace(workspace);
                updatedTeams.add(team);
            }
        }
        for (Team oldTeam : existingTeams) {
            if (!updatedTeams.contains(oldTeam)) {
                oldTeam.setWorkspace(null);
            }
        }
        workspace.setTeams(updatedTeams);
    }

    // Metodo che aggiorna le relazioni progetti e teams a chiamata POST
    public void updateRelationsForPostWorkspace(Workspace workspace) {
        // Per i team
        if (workspace.getTeams() != null) {
            for (Team team : workspace.getTeams()) {
                team.setWorkspace(workspace);
            }
        }

        // Per i progetti
        if (workspace.getProjects() != null) {
            for (Project project : workspace.getProjects()) {
                project.setWorkspace(workspace);
            }
        }

    }

}
