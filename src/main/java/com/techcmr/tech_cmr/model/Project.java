package com.techcmr.tech_cmr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.techcmr.tech_cmr.enums.ProjectStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    // Relazione molti a uno con team
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // Relazione uno a molti con project
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    // Relazione uno a molti con attachment
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Attachment> attachments;

    // Un project ha molte sections
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Section> sections;

    // Un project ha molte stories
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Story> stories;

    // Relazione molti a uno con workspace
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    // Relazione molti a molti con tags
    @ManyToMany
    @JoinTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @FutureOrPresent(message = "Expected end date cannot be in the past")
    private LocalDate expectedEndDate;

    @PastOrPresent(message = "Actual end date cannot be in the future")
    private LocalDate actualEndDate;

    // La data di creazione dovrebbe essere gestita dal DB
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 👉 Metodo che imposta createdAt quando l'entità viene salvata per la prima
    // volta
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // getters and setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public LocalDate getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }


    public Set<Story> getStories() {
        return this.stories;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }


}
