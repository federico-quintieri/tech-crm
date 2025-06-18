package com.techcmr.tech_cmr.dto;

import java.util.Set;

public class UserDTO {

    private Long id;
    private String username;
    private String password;  // se vuoi esporre la password nel DTO (di solito no, attenzione)
    private boolean enabled;

    private Set<Long> roleIds; // solo gli ID dei ruoli

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
