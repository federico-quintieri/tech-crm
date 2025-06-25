package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.mapper.UserMapper;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.repository.RoleRepository;
import com.techcmr.tech_cmr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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

        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        User user = userMapper.toEntity(userDTO, roleRepository);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    // UPDATE
    public void updateUser(Long id, UserDTO userDTO) {

        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        User updatedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        userMapper.updateEntityFromDto(userDTO, updatedUser, roleRepository);

        userRepository.save(updatedUser);
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
