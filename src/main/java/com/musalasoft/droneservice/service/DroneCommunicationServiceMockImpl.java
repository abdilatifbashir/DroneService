package com.musalasoft.droneservice.service;

import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Drone;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@Service
public class DroneCommunicationServiceMockImpl implements DroneCommunicationService{
    @Override
    public int getDronePercentage(Drone drone) {
        return drone.getBatteryPercentage ()-1 ;
    }
    @Override
    public DroneState getDroneState(Drone drone) {
        return drone.getState ();
    }
}
