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
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "machines_users", joinColumns = @JoinColumn(name = "machine_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<Machine> machines = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public String toString2() {
        StringBuilder str = new StringBuilder("User{" + "id=" + id + ", name='" + name + '\'' + "machines=[");
        for (Machine machine : machines) {
            str.append(machine.toString3());
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
        return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
