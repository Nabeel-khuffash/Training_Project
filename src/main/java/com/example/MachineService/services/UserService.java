package com.example.MachineService.services;

import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.MachineRepository;
import com.example.MachineService.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;

@Service
public class UserService {

    private MachineRepository machineRepository;
    private UserRepository userRepository;

    public UserService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }


    @SneakyThrows
    public User addUser(User user)
    {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new KeyAlreadyExistsException("User Name already exist");
        }
        userRepository.save(user);
        return user;
    }

    public boolean isUserIn(Long id)
    {
        if(userRepository.findById(id).isPresent()) return true;
        else return false;
    }
}
