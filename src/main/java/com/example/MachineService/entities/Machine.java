package com.example.MachineService.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Machine {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String ipAddress;

    private String location;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "machines")
    List<Task> tasks = new ArrayList<>();

    public Machine() {
    }

    public Machine(Long id) {
        this.id = id;
    }

}
