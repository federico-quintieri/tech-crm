package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.repository.RoleRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // DTO to Entity mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "roleIdsToRoles")
    User toEntity(UserDTO userDTO, @Context RoleRepository roleRepository);

    // Entity to DTO mapping
    @Mapping(target = "roleIds", source = "roles", qualifiedByName = "rolesToRoleIds")
    UserDTO toDto(User user);

    // Update existing entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "roleIdsToRoles")
    void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user, @Context RoleRepository roleRepository);

    // Convert Set<Long> -> Set<Role>
    @Named("roleIdsToRoles")
    default Set<Role> roleIdsToRoles(Set<Long> roleIds, @Context RoleRepository roleRepository) {
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        return roleIds.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    // Convert Set<Role> -> Set<Long>
    @Named("rolesToRoleIds")
    default Set<Long> rolesToRoleIds(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}
