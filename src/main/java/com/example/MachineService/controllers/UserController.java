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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public Response addUser(@RequestBody User user) {
        try {
            User result = userService.addUser(user);
            return Response.status(Response.Status.CREATED).entity(result.toString2()).build();
        } catch (KeyAlreadyExistsException keyAlreadyExistsException) {
            return Response.status(Response.Status.CONFLICT).entity(keyAlreadyExistsException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }
}
