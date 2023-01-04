package com.musalasoft.droneservice;

import com.musalasoft.droneservice.apiResource.dto.LoadDroneDto;
import com.musalasoft.droneservice.constants.DeliveryStatus;
import com.musalasoft.droneservice.constants.DroneModel;
import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Delivery;
import com.musalasoft.droneservice.model.DeliveryLoad;
import com.musalasoft.droneservice.model.Drone;
import com.musalasoft.droneservice.model.Medicine;
import com.musalasoft.droneservice.repository.DeliveryLoadRepository;
import com.musalasoft.droneservice.repository.DeliveryRepository;
import com.musalasoft.droneservice.repository.DroneRepository;
import com.musalasoft.droneservice.repository.MedicineRepository;
import com.musalasoft.droneservice.service.DroneAuditService;
import com.musalasoft.droneservice.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DispatchControllerIntegrationTest {
    @Autowired
    public DroneService droneService;

    private Delivery delivery;
    @MockBean
    private DroneRepository droneRepository;
    private  WebTestClient webTestClient;
    private DeliveryLoad deliveryLoad;
    private Medicine medicine;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private DeliveryLoadRepository deliveryLoadRepository;
    @MockBean
    private MedicineRepository medicineRepository;

    @Autowired
    ApplicationContext context;

    private Drone drone;

    @BeforeEach
    public void setup(){
            this.webTestClient = WebTestClient
                    .bindToApplicationContext(this.context)
                    .configureClient()
                    .responseTimeout(Duration.ofHours(1))
                    .build();

         drone= Drone.builder ()
                .serialNo ("TFWYSXGWTUHUE")
                .model (DroneModel.Lightweight)
                .weighLimit (800)
                .batteryPercentage (80)
                .state (DroneState.IDLE)
                .build ();
        delivery= Delivery.builder ()
                .deliveryStatus (DeliveryStatus.LOADING)
                .drone (drone)
                .loadWeight (0)
                .build ();

        medicine= Medicine.builder ()
                .name ("Plifzer")
                .code ("PL_WYDBGWIAWUXBYW")
                .weight (100)
                .image ("")
                .image ("")
                .build ();
        deliveryLoad= DeliveryLoad.builder ()
                .delivery (delivery)
                .medicine (medicine)
                .count (1)
                .build ();
    }

    @Test
    void registerDroneTest(){
        when (droneRepository.save (any (Drone.class))).thenReturn (drone);
        when( droneRepository.findTopBySerialNoAndSoftDeleteFalse (drone.getSerialNo ())).thenReturn (Optional.empty ());
        webTestClient
                .post ()
                .uri ("/api/v1/drone/create")
                .body (BodyInserters.fromValue (drone))
                .exchange ()
                .expectStatus ()
                .is2xxSuccessful ()
                .expectBody ()
                .jsonPath ("$.status").isNumber ()
                .jsonPath ("$.status").isEqualTo (200)
                .jsonPath ("$.message").isEqualTo ("Drone registered successfully");
    }


    @Test
    void loadDroneTest(){
        when (droneRepository.findByIdAndSoftDeleteFalse (any (Long.class))).thenReturn (Optional.of (drone));
        when (deliveryRepository.findDeliveryByDroneIdAndDeliveryStatusAndSoftDeleteFalse (drone.getId (),DeliveryStatus.LOADING)).thenReturn (Optional.of (delivery));
        when(medicineRepository.findMedicineByIdAndSoftDeleteFalse (anyLong ())).thenReturn (Optional.of (medicine));
        when (deliveryRepository.save (any (Delivery.class))).thenReturn (delivery);
        LoadDroneDto loadDroneDto= new LoadDroneDto (drone.getId (),1);
        webTestClient
                .post ()
                .uri ("/api/v1/drone/load/drone")
                .body (BodyInserters.fromValue (loadDroneDto))
                .exchange ()
                .expectStatus ()
                .is2xxSuccessful ()
                .expectBody ()
                .jsonPath ("$.status").isNumber ()
                .jsonPath ("$.status").isEqualTo (200)
                .jsonPath ("$.message").isEqualTo ("Loaded drone successfully");
    }

    @Test
    void getLoadedMedicationTest(){
        when(droneRepository.findByIdAndSoftDeleteFalse (anyLong ())).thenReturn (Optional.of (drone));
        drone.setState (DroneState.LOADING);
        when(deliveryLoadRepository.checkLoadedMedicationOnDrone (anyLong ())).thenReturn (List.of (deliveryLoad));
        webTestClient
                .get ()
                .uri (uriBuilder -> uriBuilder
                        .path ("/api/v1/drone/loaded/medication")
                        .queryParam ("droneId",1)
                        .build ()
                )
                .exchange ()
                .expectStatus ()
                .is2xxSuccessful ()
                .expectBody ()
                .jsonPath ("$.status").isNumber ()
                .jsonPath ("$.status").isEqualTo (200)
                .jsonPath ("$.message").isEqualTo ("Loaded medication");
    }

    @Test
    void getAvailableDroneTest(){
        Pageable pageable= PageRequest.of (0,10);
        List<Drone> droneList= List.of (drone);
        when (droneRepository.findAllByStateAndSoftDeleteFalse (DroneState.IDLE,pageable)).thenReturn (droneList);
        webTestClient
                .get ()
                .uri (uriBuilder -> uriBuilder
                        .path ("/api/v1/drone/available/drone")
                        .queryParam ("size",10)
                        .queryParam ("page",0)
                        .build ())
                .exchange ()
                .expectStatus ()
                .is2xxSuccessful ()
                .expectBody ()
                .jsonPath ("$.status").isNumber ()
                .jsonPath ("$.status").isEqualTo (200)
                .jsonPath ("$.message").isEqualTo ("Available drones");
    }

    @Test
    void getCheckPercentage(){
        when (droneRepository.findByIdAndSoftDeleteFalse (anyLong ())).thenReturn (Optional.of (drone));
        webTestClient
                .get ()
                .uri (uriBuilder -> uriBuilder
                        .path ("/api/v1/drone/check/percentage")
                        .queryParam ("droneId",6)
                        .build ()
                )

                .exchange ()
                .expectStatus ()
                .is2xxSuccessful ()
                .expectBody ()
                .jsonPath ("$.status").isNumber ()
                .jsonPath ("$.status").isEqualTo (200)
                .jsonPath ("$.message").isEqualTo ("Battery percentage");
    }
}
