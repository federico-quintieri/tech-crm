package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.mapper.RoleMapper;
import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    // Trova tutti i ruoli
    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toDTO).toList();
    }

    // Trova ruoli che contengono nome/stringa
    public List<RoleDTO> findRolesByName(String name) {
        return roleRepository.findByNameContaining(name).stream().map(roleMapper::toDTO).toList();
    }

    // Trova ruolo in base ad id
    public RoleDTO findRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return roleMapper.toDTO(role);
    }

    // Crea nuovo ruolo
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO); // converte DTO ad entity
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    // Modifica ruolo
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role updatedRole = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleMapper.updateEntityFromDto(roleDTO, updatedRole);
        return roleMapper.toDTO(updatedRole);
    }

    // Cancello ruolo
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleRepository.delete(role);
    }
}
