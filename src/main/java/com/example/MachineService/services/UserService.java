package com.example.MachineService.services;

import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.MachineRepository;
import com.example.MachineService.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private MachineRepository machineRepository;
    private UserRepository userRepository;

    public UserService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }


    public boolean addUser(User user)
    {
        Optional<User> userCheck=userRepository.findById(user.getId());
        Optional<User> userCheck2=userRepository.findByName(user.getName());
        if(userCheck.isPresent() || userCheck2.isPresent())
        {
            return false;
        }
        else {
            userRepository.save(user);
            return true;
        }
    }
}
