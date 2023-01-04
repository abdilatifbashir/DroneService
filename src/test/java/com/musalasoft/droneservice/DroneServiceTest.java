package com.musalasoft.droneservice;

import com.musalasoft.droneservice.constants.DeliveryStatus;
import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Delivery;
import com.musalasoft.droneservice.model.Drone;
import com.musalasoft.droneservice.model.DeliveryLoad;
import com.musalasoft.droneservice.model.Medicine;
import com.musalasoft.droneservice.repository.DeliveryLoadRepository;
import com.musalasoft.droneservice.repository.DeliveryRepository;
import com.musalasoft.droneservice.repository.DroneRepository;
import com.musalasoft.droneservice.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@SpringBootTest
class DroneServiceTest {
    @Autowired
    private DroneService droneService;
    @MockBean
    private DroneRepository droneRepository;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private DeliveryLoadRepository deliveryLoadRepository;
    private Drone drone;
    private Delivery delivery;

    private Medicine medicine;

    private DeliveryLoad deliveryLoad;


    @BeforeEach
    public void setupDrone(){
        drone= Drone.builder ()
                .batteryPercentage (70)
                .state (DroneState.IDLE)
                .serialNo ("4622822472847")
                .weighLimit (400)
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
    @DisplayName ("Junit test to register drone")
    void registerDroneTest(){
        when (droneRepository.save (any (Drone.class))).thenReturn (drone);
        when( droneRepository.findTopBySerialNoAndSoftDeleteFalse (drone.getSerialNo ())).thenReturn (Optional.empty ());
        Drone savedDrone=droneService.registerDrone (drone);
        assertThat(savedDrone).isInstanceOf (Drone.class).satisfies ();
        assertThat (savedDrone).isEqualTo (drone);
    }

    @Test
    @DisplayName ("Junit test to load drone ")
    void loadDroneTest(){
        when (droneRepository.findByIdAndSoftDeleteFalse (any (Long.class))).thenReturn (Optional.of (drone));
        when (deliveryRepository.findDeliveryByDroneIdAndDeliveryStatusAndSoftDeleteFalse (drone.getId (),DeliveryStatus.LOADING)).thenReturn (Optional.of (delivery));
        when (deliveryRepository.save (any (Delivery.class))).thenReturn (delivery);
        drone.setState (DroneState.LOADING);
        Delivery savedDelivery=droneService.loadDrone (drone.getId (), medicine.getId ());
        assertThat (savedDelivery).isNotNull ();
        assertThat (savedDelivery.getLoadWeight ()).isNotZero ();
    }

    @Test
    @DisplayName (("Junit test to test for loaded medication"))
    void checkLoadedMedicationTest(){
        when(droneRepository.findByIdAndSoftDeleteFalse(any (Long.class))).thenReturn (Optional.of (drone));
        drone.setState (DroneState.LOADING);
        when(deliveryLoadRepository.checkLoadedMedicationOnDrone (drone.getId ())).thenReturn (List.of (deliveryLoad));
        List<DeliveryLoad> deliveryLoads =droneService.checkLoadedMedication (drone.getId ());
        assertThat (deliveryLoads).isNotNull ();
        assertThat (deliveryLoads).isInstanceOf (List.class);
    }
    @Test
    @DisplayName ("Junit test to test for get available drone")
    void checkAvailableDroneTest(){
        Pageable pageable= PageRequest.of (0,5);
        List<Drone> droneList= List.of (drone);
        when (droneRepository.findAllByStateAndSoftDeleteFalse (DroneState.IDLE,pageable)).thenReturn (droneList);
        assertThat (droneService.checkAvailableDrones (pageable)).isEqualTo (droneList);
    }

    @Test
    @DisplayName ("Junit test to check Drone Percentage")
    void checkDronePercentage(){
        when (droneRepository.findByIdAndSoftDeleteFalse (anyLong ())).thenReturn (Optional.of (drone));
        assertThat (droneService.checkDronePercentage (anyLong ())).isEqualTo (drone.getBatteryPercentage ());
    }
}
