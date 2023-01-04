package com.musalasoft.droneservice.repository;

import com.musalasoft.droneservice.constants.DeliveryStatus;
import com.musalasoft.droneservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findDeliveryByDroneIdAndDeliveryStatusAndSoftDeleteFalse(long droneId, DeliveryStatus deliveryStatus);

}
