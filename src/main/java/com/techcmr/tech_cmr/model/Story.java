package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Story content is required")
    @Size(max = 1000, message = "Story content must not exceed 1000 characters")
    private String content;  // Testo del commento o descrizione evento

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // L'autore del commento / evento
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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
