package com.example.MachineService.services;

import com.example.MachineService.entities.Machine;
import com.example.MachineService.entities.Task;
import com.example.MachineService.repositories.TaskRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkService {

    //TaskService save tasks in the db
    //WorkService can only read from db
    //WorkService will be called every 20 seconds to run tasks and remove finished ones
    //WorkService send the updates needed on tasks to TaskService using kafka
    //kafka listener receive data and call a method in TaskService

    private final TaskRepository taskRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private List<Task> runningTasks = new ArrayList<>();

    public WorkService(TaskRepository taskRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void runTasks() {
        System.out.println("called");
        for (Task t : runningTasks) {
            kafkaTemplate.send("task", "COMPLETED," + t.getId());
        }
        runningTasks = new ArrayList<>();
        List<Task> tasks = taskRepository.findNextTasks();
        for (Task task : tasks) {
            boolean machinesFree = true;
            for (Machine m : task.getMachines()) {
                if (m.getPoolNumber() > 2) {
                    machinesFree = false;
                    break;
                }
            }
            if (!machinesFree) continue;
            runningTasks.add(task);
            kafkaTemplate.send("task", "IN_PROGRESS," + task.getId());
        }
    }

}
