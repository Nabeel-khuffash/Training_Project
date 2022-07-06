package com.example.MachineService.services;

import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    public User addUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new KeyAlreadyExistsException("User Name already exist");
        }
        userRepository.save(user);
        return user;
    }

    public boolean isUserIn(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
