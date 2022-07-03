package com.example.MachineService.controllers;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.User;
import com.example.MachineService.services.MachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tenant")
@Controller
public class MachineController {

    private MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    //@GetMapping("/{userId}/machine/device/{deviceId}")
    @GetMapping("/{userId}/machine/device/{deviceId}")
    public String addMachineRequest(Model model, @PathVariable Long userId, @PathVariable Long deviceId)
    {
        Machine machine=new Machine();
        machine.setId(deviceId);
        machine.setUser(new User(userId));
        model.addAttribute("Machine", machine);
        model.addAttribute("id", deviceId);
        model.addAttribute("userId", userId);
        return "AddDevice"; //add machine form
    }

    @PostMapping("/{userId}/machine/device/{deviceId}")
    String addMachine(Machine machine, @PathVariable Long userId, @PathVariable Long deviceId)
    {
        machine.setId(deviceId);
        machine.setUser(new User(userId));
        machineService.addMachine(machine);
        return "redirect:/test"; //redirect to a valid location
    }

    @DeleteMapping("/tenant/{userId}/machine/device/")
    String deleteMachine(@PathVariable Long userId){

        boolean isFound =machineService.deleteMachines(userId);
        if(isFound)
        {

        }
        else {

        }

        return "";
    }

    @PutMapping("/tenant/{userId}/machine/device/{deviceId}")
    String updateMachine(@PathVariable Long userId, @PathVariable Long deviceId, @RequestBody Machine machine)
    {
        machineService.updateMachine(machine);

        return ""; //redirect to a valid location
    }
//.

}
