package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.mapper.UserMapper;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

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
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    // UPDATE
    public void udpdatedUser(Long id, UserDTO userDTO) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setPassword(userDTO.getPassword());

        userRepository.save(updatedUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
