package com.example.MachineService.controllers;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.Task;
import com.example.MachineService.services.TaskService;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;

@RequestMapping("/")
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("user/{userId}/job/task/")
    Response addTasks(@RequestBody Task[] tasksArray, @PathVariable Long userId) {
        try {
            //checking if there is a conflict with the parameters
            List<Task> tasks = Arrays.asList(tasksArray);
            Map<String, String> map= new HashMap<>();
            Void a;
            for (Task task : tasks) {
                for (Machine machine: task.getMachines()) {
                    if(map.containsKey("t"+task.getName()+"m"+machine.getId())) throw new ParameterStrategyException("conflict in input tasks!");
                    map.put("t"+task.getName()+"m"+machine.getId(),"");
                }
            }
            map.clear();
            List<Task> result = taskService.addTasks(tasks, userId);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (ParameterStrategyException parameterStrategyException) {
            return Response.status(Response.Status.CONFLICT).entity(parameterStrategyException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }

    @DeleteMapping("tenant/{userId}/job/task/")
    Response deleteTasks(@RequestBody long[] idsArray, @PathVariable Long userId) {
        try {
            //checking if there is a conflict with the parameters
            List<Long> ids = new ArrayList<>();
            for (long l : idsArray) {
                ids.add(l);
            }
            Map<Long,String> map= new HashMap<>();
            for(Long id:ids)
            {
                if(map.containsKey(id)) throw new ParameterStrategyException("there is 2 tasks have the same id!");
                map.put(id,"");
            }
            map.clear();
            taskService.deleteTasks(ids, userId);
            return Response.status(Response.Status.ACCEPTED).entity("all tasks deleted!").build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (ParameterStrategyException parameterStrategyException) {
            return Response.status(Response.Status.CONFLICT).entity(parameterStrategyException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }

    @PostMapping("tenant/{userId}/job/task/{taskId}")
    Response reSubmit(@PathVariable Long userId, @PathVariable Long taskId) {
        try {
            Task result = taskService.reSubmit(userId, taskId);
            return Response.status(Response.Status.ACCEPTED).entity(result).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }

    @GetMapping("tenant/{userId}/job/task/{taskId}")
    Response getTask(@PathVariable Long userId, @PathVariable Long taskId) {
        try {
            Task result = taskService.getTask(userId, taskId);
            return Response.status(Response.Status.FOUND).entity(result).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }
}
