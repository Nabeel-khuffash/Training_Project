package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.MachineRepository;
import com.example.MachineService.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MachineService {

    private MachineRepository machineRepository;
    private UserRepository userRepository;

    public MachineService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    public void addMachine(Machine machine)
    {
        userRepository.save(new User(machine.getUser().getId()));
        machineRepository.save(machine);
    }

    public boolean deleteMachines(Long userId)
    {
        Optional<User> user= userRepository.findById(userId);
        if(user.isPresent())
        {
            machineRepository.deleteAllByUserId(userId);
            return true;
        }
        return false;
    }

    public void updateMachine(Machine machine)
    {
        addMachine(machine);
    }
}
