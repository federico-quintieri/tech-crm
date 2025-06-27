package com.techcmr.tech_cmr.dto;

import java.util.Set;

public class SectionDTO {

    private Long id;
    private String name;

    private Long projectId;

    private Set<Long> taskIds;  // lista degli ID delle task

    // Getters e Setters

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Set<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<Long> taskIds) {
        this.taskIds = taskIds;
    }
}