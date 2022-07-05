package com.example.MachineService.controllers;

import com.example.MachineService.entities.Task;
import com.example.MachineService.services.TaskService;
import com.example.MachineService.services.UserService;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

@RequestMapping("/")
@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("user/{userId}/job/task")
    Response addTask(@RequestBody Task task, @PathVariable Long userId)
    {
        try{
            Task result= taskService.addTask(task, userId);
            return Response.status(Response.Status.CREATED).entity(task).build();
        }
        catch (NotFoundException notFoundException){
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        }
        catch (ParameterMisuseException parameterMisuseException){
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        }
    }


}
