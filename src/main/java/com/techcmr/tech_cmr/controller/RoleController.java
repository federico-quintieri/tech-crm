package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.RoleDTO;
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
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    // READ
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.findAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roles);
    }

    // READ
    @GetMapping("/name/{name}")
    public ResponseEntity<List<RoleDTO>> getRoleByName(@PathVariable String name) {
        List<RoleDTO> roles = roleService.findRolesByName(name);

        if (roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roles);
    }

    // READ SHOW
    @GetMapping("/id/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {

        RoleDTO roleDTO = roleService.findRoleById(id);

        if (roleDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roleDTO);

    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id); // Imposta l'id ricevuto da url nell'oggetto body DTO
        RoleDTO updatedRole = roleService.updateRole(roleDTO);
        return ResponseEntity.ok(updatedRole);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
