package com.musalasoft.droneservice.service;

import com.musalasoft.droneservice.model.Delivery;
import com.musalasoft.droneservice.model.Drone;
import com.musalasoft.droneservice.model.DeliveryLoad;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DroneService {
    /**
     *
     * @param drone  object to register
     * @return  return newly registered drone
     */

    Drone registerDrone(Drone drone);
    /**
     * @param droneId  Drone Id
     * @param medicineId Medicine Id
     * @return delivery order details
     */
    Delivery loadDrone(long droneId, long medicineId);
    /**
     *
     * @param drone drone id to for loaded medicines
     * @return a list of medicines with loaded quantities
     */

    List<DeliveryLoad> checkLoadedMedication(long drone);

    /**
     *
     * @param pageable get results as pages
     * @return a list of available drones
     */
    List<Drone> checkAvailableDrones(Pageable pageable);

    /**
     *
     * @param drone drone Id
     * @return percentage of drone
     */
    int checkDronePercentage(long drone);


}
