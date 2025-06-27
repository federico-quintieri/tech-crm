package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.mapper.UserMapper;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.relations.UserRelationManager;
import com.techcmr.tech_cmr.repository.RoleRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
import com.techcmr.tech_cmr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRelationManager userRelationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // READ
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    // SHOW
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    // CREATE
    public UserDTO createUser(UserDTO userDTO) {
        // 1 encoding password
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        // 2 Dto a entity
        User user = userMapper.toEntity(userDTO, roleRepository,storyRepository);

        // 3 Metodo che aggiorna relazioni one to many
        userRelationManager.updateRelationsForPostUser(user);

        // 4 Salvo il progetto
        User savedUser = userRepository.save(user);

        // 5 Restituisco il dto
        return userMapper.toDto(savedUser);
    }

    // UPDATE
    public void updateUser(Long id, UserDTO userDTO) {

        // 1 Encoding passord
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        // 2 Trovo lo user esistente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 3 Mergiare entity a dto
        userMapper.updateEntityFromDto(userDTO, existingUser, roleRepository);

        // 4 Metodi per aggiornare le relazioni
        userRelationManager.updateStoriesForUser(existingUser, userDTO);

        // 5 Salva lo user
        userRepository.save(existingUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

    // VERIFY METHOD
    public String verify(UserDTO userDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        if (authentication.isAuthenticated())
            return jwtService.generateToken(userDTO.getUsername());
        return "Fail";
    }
}
