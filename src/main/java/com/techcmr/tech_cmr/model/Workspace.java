package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

// Ogni workspace ha più team e più progetti, è il contenitore più alto-

@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Workspace name is required")
    @Size(max = 150, message = "Workspace name must not exceed 150 characters")
    private String name;

    // One workspace has many teams
    @OneToMany(mappedBy = "workspace",  cascade = CascadeType.ALL)
    private List<Team> teams;

    // One workspace has many projects
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Project> projects;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
