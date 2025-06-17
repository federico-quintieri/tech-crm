package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Nome del tag, es. "Urgente", "Frontend", "Bug"

    // Relazione molti a molti con Task
    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;

    // Relazione molti a molti con Project
    @ManyToMany(mappedBy = "tags")
    private List<Project> projects;

    // Getters e setters

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
