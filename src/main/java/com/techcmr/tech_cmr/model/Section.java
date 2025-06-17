package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Nome della sezione, es. "To Do", "In Progress", "Done"

    // Relazione many to une, ogni sezione appartiene a un progetto
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Relazione uno a molti, una sezione ha molte task
    @OneToMany(mappedBy = "section")
    private List<Task> tasks;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
