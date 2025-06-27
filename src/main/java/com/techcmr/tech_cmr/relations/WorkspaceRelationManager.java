package com.techcmr.tech_cmr.relations;

import org.springframework.stereotype.Component;

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

    // Metodo che aggiorna relazione Teams a workspace
}
