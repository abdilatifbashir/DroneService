package com.musalasoft.droneservice.model;

import com.musalasoft.droneservice.common.BaseEntity;
import com.musalasoft.droneservice.constants.DroneModel;
import com.musalasoft.droneservice.constants.DroneState;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@Entity
@Table(name="tb_drone")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Drone  extends BaseEntity {
    @Size(min = 3,max = 100)
    private String serialNo;
    @Enumerated(EnumType.STRING)
    private DroneModel model;
    @DecimalMax (value = "500")
    private double weighLimit;
    @Max (value=100)
    private int batteryPercentage;
    @Enumerated(EnumType.STRING)
    private DroneState state;
}
