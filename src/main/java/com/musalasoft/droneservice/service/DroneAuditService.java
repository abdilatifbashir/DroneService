package com.musalasoft.droneservice.service;

import com.musalasoft.droneservice.model.DroneAudit;

import java.util.Date;
import java.util.List;

public interface DroneAuditService {
    /**
     *  Schedular task to check for drone battery levels
     */
    void checkDronePercentages();

    /**
     * @param droneId Drone Id
     * @param startDate start date of the logs
     * @param endDate end date of the logs
     * @return List of Drone Audit
     */
    List<DroneAudit> getDroneAuditByTimeRange(long droneId, Date startDate, Date endDate);
}