package com.techcmr.tech_cmr.dto;

import java.util.List;

public class WorkspaceDTO {

    private Long id;
    private String name;

    private List<Long> teamIds;     // solo ID dei team
    private List<Long> projectIds;  // solo ID dei progetti

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

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }
}