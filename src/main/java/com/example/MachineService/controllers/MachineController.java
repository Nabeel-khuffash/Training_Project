package com.example.MachineService.controllers;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.services.MachineService;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

@RequestMapping("/tenant")
@RestController
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping("/{userId}/machine/device/")
    Response addMachine(@RequestBody Machine machine, @PathVariable Long userId) {
        try {
            Machine result = machineService.addMachine(machine, userId);
            return Response.status(Response.Status.CREATED).entity(result.toString2()).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }

    @PutMapping("/{userId}/machine/device/{deviceId}")
    Response updateMachine(@RequestBody Machine machine, @PathVariable Long userId) {
        try {
            Machine result = machineService.updateMachine(machine, userId);
            return Response.status(Response.Status.CREATED).entity(result.toString2()).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }

    @DeleteMapping("/{userId}/machine/device/{deviceId}")
    Response deleteMachine(@PathVariable Long userId, @PathVariable Long deviceId) {
        try {
            machineService.deleteMachine(userId, deviceId);
            return Response.status(Response.Status.ACCEPTED).entity("Device deleted").build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }
}
