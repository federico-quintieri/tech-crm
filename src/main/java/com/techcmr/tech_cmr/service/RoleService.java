package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.mapper.RoleMapper;
import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Restituisce tutti i ruoli esistenti sotto forma di DTO.
     * Se la lista è vuota, lancia un'eccezione 404.
     */
    public List<RoleDTO> findAllRoles() {
        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found");
        }

        // Converte la lista di entità Role in lista di DTO
        return roles.stream()
                .map(RoleMapper::toDTO)
                .toList();
    }

    // Trova ruoli che contengono nome/stringa
    public Optional<RoleDTO> findRoleByName(String name) {

        Optional<Role> role = roleRepository.findByNameContaining(name);

        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        // Converte Optional<Role> in Optional<RoleDTO>
        return role.map(RoleMapper::toDTO);
    }

    // Crea nuovo ruolo
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO); // converte DTO ad entity
        Role saved = roleRepository.save(role); // salva nel DB
        return RoleMapper.toDTO(saved);
    }

    // Modifica ruolo
    public RoleDTO updateRole(RoleDTO roleDTO) {
        // Verifica che il ruolo esista prima di aggiornare
        roleRepository.findById(roleDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        Role role = RoleMapper.toEntity(roleDTO); // Converto DTO a entity
        Role saved = roleRepository.save(role); // Salvo entity nel db
        return RoleMapper.toDTO(saved); // Converto entity in dto e la ritorno
    }

    // Cancello ruolo
    public void deleteRole(Long id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        roleRepository.deleteById(id);
    }
}
