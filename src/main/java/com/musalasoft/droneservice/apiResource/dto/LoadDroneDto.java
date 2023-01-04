package com.musalasoft.droneservice.apiResource.dto;

import lombok.Data;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@Data
public class LoadDroneDto {
    private final long droneId;
    private final long medicineId;
}
