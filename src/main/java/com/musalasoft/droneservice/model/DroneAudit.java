package com.musalasoft.droneservice.model;

import com.musalasoft.droneservice.common.BaseEntity;
import com.musalasoft.droneservice.constants.DroneState;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Abdilatif
 * @created 10/12/2022
 **/
@Entity
@Table(name="tb_drone_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DroneAudit extends BaseEntity {
    @ManyToOne
    private Drone drone;
    private int batteryPercentage;
    @Enumerated
    private DroneState droneState;
}
