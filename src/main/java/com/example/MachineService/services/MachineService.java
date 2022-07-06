package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.MachineRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

@Service
public class MachineService {

    private MachineRepository machineRepository;

    private UserService userService;

    public MachineService(MachineRepository machineRepository, UserService userService) {
        this.machineRepository = machineRepository;
        this.userService = userService;
    }

    public Machine addMachine(Machine machine, Long userid)
    {
        if(!userService.isUserIn(userid)) {
            throw new NotFoundException("User not found!");
        }
        machine.setUser(new User(userid));
        machineRepository.save(machine);
        return machine;
    }

    public Machine updateMachine(Machine machine, Long userid)
    {
        if(!userService.isUserIn(userid)) {
            throw new NotFoundException("User not found!");
        }
        else if(!machineRepository.findById(machine.getId()).isPresent()) {
            throw new NotFoundException("device Not found!");
        }
        machine.setUser(new User(userid));
        machineRepository.save(machine);
        return machine;
    }

    public void deleteMachine(Long userId, Long deviceId)
    {
        if(!userService.isUserIn(userId)) {
            throw new NotFoundException("User not found!");
        }
        if(!machineRepository.findById(deviceId).isPresent()){
            throw new NotFoundException("device Not found!");
        }
        machineRepository.deleteById(deviceId);
    }

}
