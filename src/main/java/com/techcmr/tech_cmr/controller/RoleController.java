package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
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

    // GET all roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.findAllRoles();
        return roles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(roles);
    }

    // GET roles by name (filter)
    @GetMapping("/search")
    public ResponseEntity<List<RoleDTO>> getRolesByName(@RequestParam("name") String name) {
        List<RoleDTO> roles = roleService.findRolesByName(name);
        return roles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(roles);
    }

    // GET role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        try {
            RoleDTO role = roleService.findRoleById(id);
            return ResponseEntity.ok(role);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST create new role
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.createRole(roleDTO);
        return ResponseEntity.status(201).body(created);
    }

    // PUT update existing role
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO updated = roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
