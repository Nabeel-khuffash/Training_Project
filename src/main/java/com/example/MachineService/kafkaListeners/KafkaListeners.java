package com.example.MachineService.kafkaListeners;

import com.example.MachineService.services.TaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final TaskService taskService;

    public KafkaListeners(TaskService taskService) {
        this.taskService = taskService;
    }

    @KafkaListener(topics = "task", groupId = "id1")
    public void listener(String str) {
        taskService.updateTaskStatus(str);
    }
}
