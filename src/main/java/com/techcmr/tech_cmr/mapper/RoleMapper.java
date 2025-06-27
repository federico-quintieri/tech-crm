package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.model.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromDto(RoleDTO role, @MappingTarget Role entity);
}