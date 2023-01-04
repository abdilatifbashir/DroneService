package com.musalasoft.droneservice.apiResource;

import com.musalasoft.droneservice.apiResource.dto.LoadDroneDto;
import com.musalasoft.droneservice.model.Delivery;
import com.musalasoft.droneservice.model.DeliveryLoad;
import com.musalasoft.droneservice.model.Drone;
import com.musalasoft.droneservice.model.DroneAudit;
import com.musalasoft.droneservice.service.DroneAuditService;
import com.musalasoft.droneservice.service.DroneService;
import com.musalasoft.droneservice.util.UniversalResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drone")
public class DispatchController {
    private final DroneService droneService;
    private final DroneAuditService droneAuditService;

    /**
     * @param drone Request body to create drone
     * @return Mono Response entity with a universal response format with created drone details
     */
    @PostMapping("/create")
    public Mono<ResponseEntity<UniversalResponse>> registerDrone(@RequestBody Drone drone){
        return Mono.fromCallable (()-> {
            Drone registeredDrone= droneService.registerDrone (drone);
            UniversalResponse response= UniversalResponse.builder ().status (200)
                    .data (registeredDrone)
                    .message ("Drone registered successfully").build ();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     *
     * @param loadDroneDto load dto class
     * @return Mono Response entity with a universal response format with Delivery order details
     */
    @PostMapping("/load/drone")
    public Mono<ResponseEntity<UniversalResponse>> loadDrone(@RequestBody LoadDroneDto loadDroneDto){
        return Mono.fromCallable (()-> {
            Delivery delivery= droneService.loadDrone (loadDroneDto.getDroneId (),loadDroneDto.getMedicineId ());
            UniversalResponse response= UniversalResponse.builder().status (200).message ("Loaded drone successfully")
                    .data (delivery)
                    .build();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     *
     * @param droneId drone id for which to check load
     * @return Mono Response entity with a universal response format with loaded medication
     */

    @GetMapping("/loaded/medication")
    public Mono<ResponseEntity<UniversalResponse>> getLoadedMedication(@RequestParam long droneId){
        return Mono.fromCallable (()-> {
            List<DeliveryLoad> deliveryLoad= droneService.checkLoadedMedication (droneId);
            UniversalResponse response= UniversalResponse.builder()
                    .status (200)
                    .message ("Loaded medication")
                    .data (deliveryLoad)
                    .build();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     *
     * @param size size of records to get
     * @param page paged number of records
     * @return Mono Response entity with a universal response format with a list of available drones
     */
    @GetMapping("/available/drone")
    public Mono<ResponseEntity<UniversalResponse>> getAvailableDrone(@RequestParam int size, @RequestParam int page){
        return Mono.fromCallable (()-> {
            Pageable pageable= PageRequest.of (page,size);
            List<Drone> availableDrones= droneService.checkAvailableDrones (pageable);
            UniversalResponse response= UniversalResponse.builder()
                    .status (200)
                    .message ("Available drones")
                    .data (availableDrones)
                    .build();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     *
     * @param droneId drone Id for which to check battery percentage
     * @return Mono Response entity with a universal response format
     */
    @GetMapping("/check/percentage")
    public Mono<ResponseEntity<UniversalResponse>> checkPercentage(@RequestParam int droneId){
        return Mono.fromCallable (()-> {
            int batteryPerc= droneService.checkDronePercentage (droneId);
            UniversalResponse response= UniversalResponse.builder()
                    .status (200)
                    .message ("Battery percentage")
                    .data (Map.of("battery", batteryPerc))
                    .build();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

    /**
     *
     * @param droneId drone Id
     * @param startDate Start Date for the logs
     * @param endDate  End Date for the logs range
     * @return
     */
    @GetMapping("/audit/logs")
    public Mono<ResponseEntity<UniversalResponse>>getDroneAuditLogs(@RequestParam long droneId,
                                                                    @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                                    @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        return Mono.fromCallable (()-> {
            List<DroneAudit> droneAudits= droneAuditService.getDroneAuditByTimeRange (droneId,startDate,endDate);
            UniversalResponse response= UniversalResponse.builder()
                    .status (200)
                    .message ("Drone Audit list")
                    .data (droneAudits)
                    .build();
            return ResponseEntity.ok ().body (response);
        }).publishOn (Schedulers.boundedElastic ());
    }

}
