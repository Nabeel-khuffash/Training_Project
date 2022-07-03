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
    @GetMapping("/{userId}/machine/device/add")
    public String addMachineRequest(Model model, @PathVariable Long userId)
    {
        if(!machineService.isUserIn(userId))
        {
            model.addAttribute("msg","user Id is invalid");
            return "fails";
        }
        model.addAttribute("Machine", new Machine());
        model.addAttribute("userId", userId);
        return "AddDevice"; //add machine form
    }

    @PostMapping("/{userId}/machine/device/add")
    String addMachine(Machine machine, @PathVariable Long userId, Model model)
    {
        //this may happen only if post request made out of ui
        Integer response =machineService.addMachine(machine, userId);
        if(response==2)
        {
            model.addAttribute("msg","Something went wrong");
            return "failed";
        }
        else if(response==1)
        {
            model.addAttribute("failed","true");
            model.addAttribute("Machine", new Machine());
            model.addAttribute("userId", userId);
            return "AddDevice";
        }
        model.addAttribute("success","true");
        model.addAttribute("Machine", new Machine());
        model.addAttribute("userId", userId);
        return "AddDevice";
    }

    @GetMapping("/{userId}/machine/device/delete")
    String deleteMachinePage(@PathVariable Long userId, Model model) {
        if(!machineService.isUserIn(userId))
        {
            model.addAttribute("msg","invalid user id");
            return "fails";
        }
        else {
            model.addAttribute("machine",new Machine());
            return"DeleteDevice";
        }
    }
    @DeleteMapping("/")
    String test(Model model)
    {
        model.addAttribute("msg","worked");
        return "fails";
    }

    @DeleteMapping("/{userId}/machine/device/{string}")
    String deleteMachine(@PathVariable Long userId, @PathVariable String string, Model model){
        System.out.println("entered");
        //Long deviceId = Long.parseLong(string.substring(string.indexOf('=')+1,string.length()));
        Integer response = machineService.deleteMachines(userId, 22L);
        if(response==2)
        {
            model.addAttribute("msg", "invalid user id!");
            return "fails";
        }
        else if(response==1)
        {
            model.addAttribute("failed","true");
            return"DeleteDevice";
        }
        else {
            model.addAttribute("success","true");
            return"DeleteDevice";
        }
    }

    @PutMapping("/{userId}/machine/device/{deviceId}")
    String updateMachine(@PathVariable Long userId, @PathVariable Long deviceId, @RequestBody Machine machine)
    {
        machineService.updateMachine(machine);

        return ""; //redirect to a valid location
    }
//.

}
