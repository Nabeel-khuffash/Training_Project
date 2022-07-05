package com.example.MachineService.entities;

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

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String type;

    private String description;

    @ManyToMany
    private List<Machine> machines= new ArrayList<>();

    private Integer priority;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    private Integer numberOfTrails=0;

    private String status="pending";

    private LocalDateTime nextExecutionTime;

    private LocalDateTime lastExecutionTime;

    @ManyToOne
    private User user;

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }
}
