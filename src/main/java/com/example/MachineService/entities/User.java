package com.example.MachineService.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {

    public User(Long id) {
        this.id = id;
    }

    public User(){}

    public User(Long id, String username) {
        this.id = id;
        this.name = username;
    }

    @Id
    private Long id;

    private String name;

    @OneToMany
    private List<Machine> machines=new ArrayList<>();
}
