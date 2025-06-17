package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAllRoles() {
        List<Role> roles = roleRepository.findAll();

        // Restituisco un eccezione not found + message se non trovo i ruoli
        if (roles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found");
        }

        // Altrimenti restituisco i ruoli
        return roles;
    }
}
