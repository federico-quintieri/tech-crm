package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;

import java.util.List;

// Ogni workspace ha più team e più progetti, è il contenitore più alto-

@Entity
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // One workspace has many teams
    @OneToMany(mappedBy = "workspace")
    private List<Team> teams;

    // One workspace has many projects
    @OneToMany(mappedBy = "workspace")
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
