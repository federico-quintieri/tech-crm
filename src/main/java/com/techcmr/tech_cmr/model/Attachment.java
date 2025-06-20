package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    private String fileName;    // Nome del file originale

    @NotBlank(message = "File type is required")
    @Size(max = 50, message = "File type must not exceed 50 characters")
    private String fileType;    // Tipo MIME o estensione

    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "File URL must not exceed 500 characters")
    private String url;         // Link o percorso al file salvato (es. su server o cloud)

    // Allegato a una task (opzionale)
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    // Allegato a un progetto (opzionale)
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
