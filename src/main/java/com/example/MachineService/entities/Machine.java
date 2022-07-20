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

    private int poolNumber = 0;

    @ManyToMany(mappedBy = "machines")
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "tasks_machines", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "machine_id", referencedColumnName = "id"))
    List<Task> tasks = new ArrayList<>();

    public Machine() {
    }

    public Machine(Long id) {
        this.id = id;
    }


    //toString without looping forever since machine have users and users have machines
    public String toString2() {
        StringBuilder str = new StringBuilder("Machine{" + "id=" + id + ", name='" + name + '\'' + ", ipAddress='" + ipAddress + '\'' + ", location='" + location + '\'' + ", users=[");
        for (User user : users) {
            str.append(user.toString3());
            str.append(',');
        }
        str.deleteCharAt(str.length() - 1);
        str.append("], tasks=[");
        for (Task task : tasks) {
            str.append(task.toString3());
            str.append(',');
        }
        str.deleteCharAt(str.length() - 1);
        str.append("]}");
        return str.toString();
    }

    //toString3 convert object to string without the objects inside
    public String toString3() {
        return "Machine{" + "id=" + id + ", name='" + name + '\'' + ", ipAddress='" + ipAddress + '\'' + ", location='" + location + '\'' + '}';
    }
}
