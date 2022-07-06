package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.Task;
import com.example.MachineService.entities.User;
import com.example.MachineService.repositories.TaskRepository;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public List<Task> addTasks(Task[] tasksArray, Long userId)
    {
        if (!userService.isUserIn(userId)) throw new NotFoundException("User Id not found");
        List<Task> tasks = Arrays.asList(tasksArray);
        for (Task task: tasks) {
            List<Machine> machines = task.getMachines();
            if (machines.size() == 0) throw new ParameterMisuseException("You have to enter at least 1 machine to every task!");
            for (Machine machine : machines) {
                if (!machineService.isMachineIn(machine.getId())) throw new NotFoundException("Not all machines found");
            }
            //for every machine task will use, it will check if the machine have already running task with the same name and user
            for (Machine machine : machines) {
                //realMachine is the real machine object you entered its id
                Machine realMachine = machineService.findMachineById(machine.getId()).get();
                for (Task usedTask : realMachine.getTasks()) {
                    //usedTask is a task that is already in the database and are running in that machine
                    if (task.getName() == usedTask.getName() && task.getUser() == usedTask.getUser() && usedTask.getStatus() != "final")
                        throw new ParameterMisuseException("task name: " +task.getName()+" is already used and running/pending by this user in machine id: " + realMachine.getId());
                }
            }
        }
        //checking if there is a conflict in the entered tasks, 2 tasks have the same name and use same machine
        for(int i=0;i<tasks.size();i++)
        {
            for(int j=i+1;j<tasks.size();j++)
            {
                Task task1=tasks.get(i);
                Task task2=tasks.get(j);
                if(task1.getName()!=task2.getName()) break;
                for(int k=0;k<task1.getMachines().size();k++)
                    for(int l=0;l<task2.getMachines().size();l++)
                    {
                        if(task1.getMachines().get(k).getId()==task2.getMachines().get(l).getId())
                            throw new ParameterStrategyException("There is a conflict, 2 tasks (you entered) use the same name and have the same machines");
                    }
            }
        }
        for (Task task: tasks) {
            List<Machine> machines = task.getMachines();
            for (Machine machine : machines) {
                machineService.findMachineById(machine.getId()).get().getTasks().add(new Task(task.getId()));
            }
            task.setUser(new User(userId));
            taskRepository.save(task);
        }
        return tasks;
    }

    public void deleteTasks(long[] idsArray, Long userId) {
        List<Long> ids= new ArrayList<>();
        for (long l: idsArray) {
            ids.add(l);
        }
        for (Long id: ids) {
            //checking if tasks are in, if there is any error get function will throw an exception
            getTask(userId, id);
        }
        //check if 2 ids are the same
        for(int i=0;i<ids.size();i++)
            for(int j=i+1;j<ids.size();j++)
                if (ids.get(i)==ids.get(j)) throw new ParameterStrategyException("There is a conflict, 2 tasks have the same ids");
        for (Long id: ids) {
            taskRepository.deleteById(id);
        }
    }

    public Task reSubmit(Long userId, Long taskId) {
        Task task = getTask(userId, taskId);
        task.setStatus("pending");
        taskRepository.save(task);
        return task;
    }

    public Task getTask(Long userId, Long taskId)
    {
        Optional<Task> task = taskRepository.findById(taskId);
        if(!task.isPresent()) throw new NotFoundException("invalid task Id");
        if(task.get().getUser().getId()!=userId) throw new ParameterMisuseException("task isn't belong to this user!");
        return task.get();
    }
}
