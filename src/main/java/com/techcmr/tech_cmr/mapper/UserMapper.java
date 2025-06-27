package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.model.Role;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.repository.RoleRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;

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
    @Mapping(target = "stories", source = "storyIds", qualifiedByName = "storyIdsToStories")
    User toEntity(UserDTO userDTO, @Context RoleRepository roleRepository, @Context StoryRepository storyRepository);

    // Entity to DTO mapping
    @Mapping(target = "roleIds", source = "roles", qualifiedByName = "rolesToRoleIds")
    @Mapping(target = "storyIds", source = "stories", qualifiedByName = "storiesToStoryIds")
    UserDTO toDto(User user);

    // Update existing entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "roleIdsToRoles")
    @Mapping(target = "stories", ignore = true)
    void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user, @Context RoleRepository roleRepository);

    // Convert Set<Long> -> Set<Role>
    @Named("roleIdsToRoles")
    default Set<Role> roleIdsToRoles(Set<Long> roleIds, @Context RoleRepository roleRepository) {
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        return roleIds.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Role not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("storyIdsToStories")
    default Set<Story> storyIdsToStories(Set<Long> storyIds, @Context StoryRepository storyRepository) {
        if (storyIds == null || storyIds.isEmpty()) {
            return null;
        }
        return storyIds.stream()
                .map(id -> storyRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Story not found with id: " + id)))
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

    @Named("storiesToStoryIds")
    default Set<Long> storiesToStoryIds(Set<Story> stories) {
        if (stories == null || stories.isEmpty()) {
            return null;
        }
        return stories.stream()
                .map(Story::getId)
                .collect(Collectors.toSet());
    }
}
