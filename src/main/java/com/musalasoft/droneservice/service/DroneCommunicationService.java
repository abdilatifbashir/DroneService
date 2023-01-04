package com.musalasoft.droneservice.service;

import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Drone;

public interface DroneCommunicationService {
    int getDronePercentage(Drone drone);

    DroneState getDroneState(Drone drone);

}
