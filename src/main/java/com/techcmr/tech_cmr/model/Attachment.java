package com.techcmr.tech_cmr.model;

import jakarta.persistence.*;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;    // Nome del file originale
    private String fileType;    // Tipo MIME o estensione
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
