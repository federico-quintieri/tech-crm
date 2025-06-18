package com.techcmr.tech_cmr.mapper;


import com.techcmr.tech_cmr.dto.RoleDTO;
import com.techcmr.tech_cmr.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);

}