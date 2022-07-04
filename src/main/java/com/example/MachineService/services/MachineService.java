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

    public Integer addMachine(Machine machine, Long userid)
    {
        if(!isUserIn(userid)) return 2;
        else if(machineRepository.findById(machine.getId()).isPresent()) return 1;
        machine.setUser(new User(userid));
        machineRepository.save(machine);
        return 0;
    }

    public Integer updateMachine(Machine machine, Long userid)
    {
        if(!isUserIn(userid)) return 2;
        else if(!machineRepository.findById(machine.getId()).isPresent()) return 1;
        machine.setUser(new User(userid));
        machineRepository.save(machine);
        return 0;
    }

    public Integer deleteMachines(Long userId, Long deviceId)
    {
        if(!isUserIn(userId)) return 2;
        if(!machineRepository.findById(deviceId).isPresent()) return 1;
        machineRepository.deleteById(deviceId);
        return 0;
    }

    public boolean isUserIn(Long id)
    {
        if(userRepository.findById(id).isPresent()) return true;
        else return false;
    }

}
