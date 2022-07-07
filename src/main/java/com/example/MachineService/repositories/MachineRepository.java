package com.example.MachineService.repositories;

import com.example.MachineService.entities.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    @Query(value = "select user_id from machines_users where machine_id=:machineId and user_id=:userId;", nativeQuery = true)
    Optional<Long> findUserByMachineId(Long machineId, Long userId);
}
