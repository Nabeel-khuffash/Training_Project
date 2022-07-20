package com.example.MachineService.repositories;

import com.example.MachineService.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select * from task where status='PENDING' order by priority asc, created_date asc;", nativeQuery = true)
    List<Task> findNextTasks();
}
