package com.example.MachineService.repositories;

import com.example.MachineService.entities.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    void deleteAllByUserId(Long userid);
}
