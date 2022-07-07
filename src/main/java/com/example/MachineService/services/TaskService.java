package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.Task;
import com.example.MachineService.entities.User;
import com.example.MachineService.enums.Status;
import com.example.MachineService.repositories.TaskRepository;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MachineService machineService;

    public TaskService(TaskRepository taskRepository, UserService userService, MachineService machineService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.machineService = machineService;
    }

    public List<Task> addTasks(List<Task> tasks, Long userId) {
        if (!userService.isUserIn(userId)) throw new NotFoundException("User Id not found");
        for (Task task : tasks) {
            List<Machine> machines = task.getMachines();
            if (machines.size() == 0)
                throw new ParameterMisuseException("You have to enter at least 1 machine to every task!");
            for (Machine machine : machines) {
                if (!machineService.isMachineIn(machine.getId())) throw new NotFoundException("Not all machines found");
            }
            //for every machine the task will use, it will check if the machine have already running task with the same name and user
            for (Machine machine : machines) {
                //realMachine is the real machine object you entered its id
                Machine realMachine = machineService.findMachineById(machine.getId()).get();
                for (Task usedTask : realMachine.getTasks()) {
                    //usedTask is a task that is already in the database and are running in that machine
                    if (task.getName().equals(usedTask.getName()) && task.getUser().equals(usedTask.getUser()) && !usedTask.getStatus().equals(Status.COMPLETED))
                        throw new ParameterMisuseException("task name: " + task.getName() + " is already used and running/pending by this user in machine id: " + realMachine.getId());
                }
            }
        }
        User user = userService.findUserById(userId).get();
        for (Task task : tasks) {
            task.setUser(user);
            user.getTasks().add(task);
            List<Machine> machines = task.getMachines();
            for (Machine machine : machines) {
                machineService.findMachineById(machine.getId()).get().getTasks().add(new Task(task.getId()));
            }
            taskRepository.save(task);
        }
        return tasks;
    }

    public void deleteTasks(List<Long> ids, Long userId) {
        for (Long id : ids) {
            //checking if tasks are in, if there is any error get function will throw an exception
            getTask(userId, id);
        }
        for (Long id : ids) {
            taskRepository.deleteById(id);
        }
    }

    public Task reSubmit(Long userId, Long taskId) {
        Task task = getTask(userId, taskId);
        if (!task.getStatus().equals(Status.COMPLETED))
            throw new ParameterMisuseException("task isn't done yet to resubmit!");
        List<Machine> machines = task.getMachines();
        for (Machine machine : machines) {
            //realMachine is the real machine object you entered its id
            Machine realMachine = machineService.findMachineById(machine.getId()).get();
            for (Task usedTask : realMachine.getTasks()) {
                //usedTask is a task that is already in the database and are running in that machine
                if (task.getName().equals(usedTask.getName()) && task.getUser().equals(usedTask.getUser()) && !usedTask.getStatus().equals(Status.COMPLETED))
                    throw new ParameterMisuseException("task name: " + task.getName() + " is already used and running/pending by this user in machine id: " + realMachine.getId());
            }
        }
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
        return task;
    }

    public Task getTask(Long userId, Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) throw new NotFoundException("invalid task Id");
        if (!task.get().getUser().getId().equals(userId))
            throw new ParameterMisuseException("task isn't belong to this user!");
        return task.get();
    }
}
