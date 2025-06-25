package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.model.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "roles", target = "roleIds")
    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(rolesFromIds(dto.getRoleIds()))")
    User toEntity(UserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);

    default Set<Long> rolesToIds(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }

    default Set<Role> rolesFromIds(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }
}