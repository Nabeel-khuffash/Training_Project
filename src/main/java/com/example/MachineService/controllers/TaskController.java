package com.example.MachineService.controllers;

import com.example.MachineService.entities.Task;
import com.example.MachineService.services.TaskService;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestMapping("/")
@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("user/{userId}/job/task/")
    Response addTasks(@RequestBody Task[] tasks, @PathVariable Long userId)
    {
        try{
            List<Task> result= taskService.addTasks(tasks, userId);
            return Response.status(Response.Status.CREATED).entity(result).build();
        }
        catch (NotFoundException notFoundException){
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        }
        catch (ParameterMisuseException parameterMisuseException){
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        }
        catch (ParameterStrategyException parameterStrategyException){
            return Response.status(Response.Status.CONFLICT).entity(parameterStrategyException.getMessage()).build();
        }
    }

    @DeleteMapping("tenant/{userId}/job/task/")
    Response deleteTasks(@RequestBody long[] ids, @PathVariable Long userId)
    {
        try{
            taskService.deleteTasks(ids, userId);
            return Response.status(Response.Status.ACCEPTED).entity("all tasks deleted!").build();
        }
        catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        }
        catch (ParameterMisuseException parameterMisuseException){
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        }
        catch (ParameterStrategyException parameterStrategyException){
            return Response.status(Response.Status.CONFLICT).entity(parameterStrategyException.getMessage()).build();
        }
    }

    @PostMapping("tenant/{userId}/job/task/{taskId}")
    Response reSubmit(@PathVariable Long userId, @PathVariable Long taskId)
    {
        try{
            Task result=taskService.reSubmit(userId, taskId);
            return Response.status(Response.Status.ACCEPTED).entity(result).build();
        }
        catch (NotFoundException notFoundException){
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        }
        catch (ParameterMisuseException parameterMisuseException){
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        }
    }

    @GetMapping("tenant/{userId}/job/task/{taskId}")
    Response getTask (@PathVariable Long userId, @PathVariable Long taskId)
    {
        try{
            Task result=taskService.getTask(userId, taskId);
            return Response.status(Response.Status.FOUND).entity(result).build();
        }
        catch (NotFoundException notFoundException){
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        }
        catch (ParameterMisuseException parameterMisuseException){
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        }
    }
}
