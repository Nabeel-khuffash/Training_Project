package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.Task;
import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.TaskRepository;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private UserService userService;

    private MachineService machineService;

    public TaskService(TaskRepository taskRepository, UserService userService, MachineService machineService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.machineService = machineService;
    }

    public Task addTask(Task task, Long userId)
    {
        if(!userService.isUserIn(userId)) throw new NotFoundException("User Id not found");
        List<Machine> machines = task.getMachines();
        if(machines.size()==0)  throw new ParameterMisuseException("You have to enter at least 1 machine!");
        for (Machine machine: machines) {
            if(!machineService.isMachineIn(machine.getId())) throw new NotFoundException("Not all machines found");
        }
        for (Machine machine: machines) {
            //realMachine is the real machine object you entered its id
            Machine realMachine = machineService.findMachineById(machine.getId()).get();
            for (Task usedTask: realMachine.getTasks()) {
                //usedTask is a task that is already in that machine
                if(task.getName()==usedTask.getName() && task.getUser()==usedTask.getUser()&&usedTask.getStatus()!="final")
                    throw new ParameterMisuseException("task is already used by this user in machine: "+realMachine.getId());
            }
        }
        for (Machine machine: machines) {
            machineService.findMachineById(machine.getId()).get().getTasks().add(new Task(task.getId()));
        }
        task.setUser(new User(userId));
        taskRepository.save(task);
        return task;
    }
}
