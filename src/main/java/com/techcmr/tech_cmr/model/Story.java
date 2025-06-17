package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;  // Testo del commento o descrizione evento

    private LocalDateTime createdAt;

    // L'autore del commento / evento
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    // Task associata (opzionale, se la story è su una task)
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    // Progetto associato (opzionale, se la story è a livello progetto)
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
