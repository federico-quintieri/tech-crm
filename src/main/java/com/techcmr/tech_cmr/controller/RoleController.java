package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// CRUD, create, read, update, delete

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    // READ
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roles);
    }

    // READ
    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Optional<Role> role = roleService.findRoleByName(name);

        if (role.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(role.get());
    }

    // UPDATE

    // DELETE

}
