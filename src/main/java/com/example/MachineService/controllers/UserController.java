package com.example.MachineService.controllers;

import com.example.MachineService.entities.User;
import com.example.MachineService.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/add")
    public String addUserPage(Model model)
    {
        model.addAttribute("User", new User());
        return "AddUser";
    }

    @PostMapping("users/add")
    public String addUser(User user, Model model)
    {
        boolean success= userService.addUser(user);
        if (success) model.addAttribute("success", "true");
        else model.addAttribute("failed", "true");
        model.addAttribute("User", new User());
        return "AddUser";
    }
}
