package com.musalasoft.droneservice.repository;

import com.musalasoft.droneservice.model.DeliveryLoad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineLoadRepository extends JpaRepository<DeliveryLoad,Long> {
}
