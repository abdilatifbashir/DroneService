package com.musalasoft.droneservice.repository;

import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Drone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone,Long> {
    List<Drone> findAllByStateAndSoftDeleteFalse(DroneState droneState, Pageable pageable);
    Optional<Drone> findTopBySerialNoAndSoftDeleteFalse(String serialNo);
    Optional<Drone> findByIdAndSoftDeleteFalse(long id);
    List<Drone> findAllBySoftDeleteFalse();

}
