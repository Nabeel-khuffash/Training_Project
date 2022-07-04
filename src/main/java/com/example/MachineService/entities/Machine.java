package com.example.MachineService.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Machine {

    public Machine(Long id, String name, String ipAddress, String location, Long userid) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.location = location;
        this.user = new User(userid);
    }

    public Machine(Long id, String name, String ipAddress, String location) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.location = location;
    }

    public Machine() {
    }

    @Id
    private Long id;

    private String name;

    private String ipAddress;

    private String location;

    @ManyToOne
    private User user;


}
