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
}
