package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.model.Role;

/**
 * RoleMapper gestisce la conversione tra l'entità Role e il DTO RoleDTO.
 * Questo separa la logica di mapping dal resto dell'applicazione,
 * rendendola riutilizzabile e facilmente testabile.
 */
public class RoleMapper {

    // Converte Role (Entity) in RoleDTO
    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    // Converte RoleDTO in Role (Entity)
    public static Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }

}
