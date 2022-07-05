package com.example.MachineService.controllers;

import com.example.MachineService.entities.User;
import com.example.MachineService.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.ws.rs.core.Response;


@RestController
@RequestMapping("/")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public Response addUser(@RequestBody User user)
    {
        try {
            User result = userService.addUser(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (KeyAlreadyExistsException keyAlreadyExistsException) {
            return Response.status(Response.Status.CONFLICT).entity(keyAlreadyExistsException.getMessage()).build();
        }
    }
}
