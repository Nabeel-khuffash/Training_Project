package com.example.MachineService.entities;

import com.example.MachineService.enums.Priority;
import com.example.MachineService.enums.Status;
import com.example.MachineService.enums.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Type type;

    private String description;

    private Status status = Status.PENDING;


    private Priority priority = Priority.FIVE;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    private int numberOfTrails = 0;

    private LocalDateTime nextExecutionTime;

    private LocalDateTime lastExecutionTime;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "machines_tasks", joinColumns = @JoinColumn(name = "machine_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"))
    private List<Machine> machines = new ArrayList<>();

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }
}
