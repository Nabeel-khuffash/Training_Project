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
    Response addTasks(@RequestBody List<Task> tasks, @PathVariable Long userId) {
        try {
            Map<Long, List<String>> map = new HashMap<>();
            for (Task task : tasks) {
                for (Machine machine : task.getMachines()) {
                    if (map.containsKey(machine.getId())) {
                        if (map.get(machine.getId()).contains(task.getName()))
                            throw new ParameterStrategyException("conflict in input tasks!");
                        map.get(machine.getId()).add(task.getName());
                    } else {
                        map.put(machine.getId(), new ArrayList<>());
                        map.get(machine.getId()).add(task.getName());
                    }
                }
            }
            map.clear();
            List<Task> result = taskService.addTasks(tasks, userId);
            StringBuilder str = new StringBuilder();
            for (Task task : result) {
                str.append(task.toString2());
            }
            return Response.status(Response.Status.CREATED).entity(str.toString()).build();
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
    Response deleteTasks(@RequestBody List<Long> ids, @PathVariable Long userId) {
        try {
            //checking if there is a conflict with the parameters
            Set<Long> idsSet = new HashSet<>(ids);
            if (ids.size() != idsSet.size()) throw new ParameterStrategyException("there is 2 tasks have the same id!");
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
            return Response.status(Response.Status.ACCEPTED).entity(result.toString2()).build();
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
            return Response.status(Response.Status.FOUND).entity(result.toString2()).build();
        } catch (NotFoundException notFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).build();
        } catch (ParameterMisuseException parameterMisuseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(parameterMisuseException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("something wrong happened").build();
        }
    }
}
