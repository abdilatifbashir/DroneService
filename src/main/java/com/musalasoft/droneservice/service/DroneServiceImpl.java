package com.musalasoft.droneservice.service;

import com.musalasoft.droneservice.Exceptions.IllegalOperationException;
import com.musalasoft.droneservice.Exceptions.ItemAlreadyExistException;
import com.musalasoft.droneservice.Exceptions.ItemNotFoundException;
import com.musalasoft.droneservice.constants.DeliveryStatus;
import com.musalasoft.droneservice.constants.DroneState;
import com.musalasoft.droneservice.model.Delivery;
import com.musalasoft.droneservice.model.Drone;
import com.musalasoft.droneservice.model.DeliveryLoad;
import com.musalasoft.droneservice.model.Medicine;
import com.musalasoft.droneservice.repository.DeliveryLoadRepository;
import com.musalasoft.droneservice.repository.DeliveryRepository;
import com.musalasoft.droneservice.repository.DroneRepository;
import com.musalasoft.droneservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Abdilatif
 * @created 10/12/2022
 **/
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService{
    private final DroneRepository droneRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryLoadRepository deliveryLoadRepository;
    private final MedicineRepository medicineRepository;
    /**
     *
     * @param drone  object to register
     * @return newly created drone
     * <p> Checks for duplicate drone by serial number </p>
     */
    @Override
    public Drone registerDrone(Drone drone) {
       Drone duplicate= droneRepository.findTopBySerialNoAndSoftDeleteFalse (drone.getSerialNo ())
               .orElse (null);
       if(duplicate!=null){
           throw new ItemAlreadyExistException ("Drone exists by serial number");
       }
       return  droneRepository.save (drone);
    }
    /**
     * @param droneId  Drone Id
     * @param medicineId medicineId load to pack into drone
     * @return boolean indicating whether drone loading was successful or
     */
    @Transactional
    @Override
    public Delivery loadDrone(long droneId, long medicineId) {
        Drone drone = droneRepository.findByIdAndSoftDeleteFalse (droneId).orElse (null);
        if(drone==null)
            throw new ItemNotFoundException ("Drone not found");
        // check drone for percentage levels
        if(drone.getBatteryPercentage ()<25){
            throw new IllegalOperationException ("Cannot load drone, battery percentage is below 25%");
        }
        //check if medicine with given id exists
        Medicine medicine= medicineRepository.findMedicineByIdAndSoftDeleteFalse (medicineId).orElse (null);
        if(medicine==null){
            throw new ItemNotFoundException ("Medicine not found");
        }
        // check if drone is in loading state or is in idle
        if(!(drone.getState ()== DroneState.LOADING || drone.getState ()==DroneState.IDLE)){
            throw new IllegalOperationException ("Drone is not in idle or loading state ");
        }
        // check for drone delivery with status loading
        Delivery loadingDelivery=deliveryRepository.findDeliveryByDroneIdAndDeliveryStatusAndSoftDeleteFalse(droneId, DeliveryStatus.LOADING).orElse (null);
        if(loadingDelivery==null ){
            loadingDelivery= Delivery.builder ()
                    .deliveryStatus (DeliveryStatus.LOADING)
                    .drone (drone).build ();
            loadingDelivery=deliveryRepository.save (loadingDelivery);
        }
        // check if medicine load exceeds current load on drone
        double weightAfterLoad= drone.getWeighLimit ()-loadingDelivery.getLoadWeight ();
        if(weightAfterLoad<0){
            throw  new IllegalOperationException ("Medicine load exceed Drone max limit ");
        }
        //load delivery into drone
        return loadDeliveryLoadIntoDrone (loadingDelivery, medicine);
    }
    private Delivery loadDeliveryLoadIntoDrone(Delivery delivery,Medicine medicine){
        //add medicine to delivery load
        DeliveryLoad deliveryLoad= deliveryLoadRepository.findDeliveryLoadByDeliveryIdAndMedicineIdAndSoftDeleteFalse (delivery.getId (),medicine.getId ())
                .orElse (null);
        if(deliveryLoad!=null){
            deliveryLoad.setCount (deliveryLoad.getCount ()+1);
        }else{
            deliveryLoad= DeliveryLoad.builder ()
                    .delivery (delivery)
                    .medicine (medicine)
                    .count (1)
                    .build ();
        }
        Drone drone= delivery.getDrone ();
        //change drone status to loading
        if(drone.getState ()!= DroneState.LOADING) {
            drone.setState (DroneState.LOADING);
            droneRepository.save (drone);
        }
        deliveryLoadRepository.save (deliveryLoad);
        delivery.setLoadWeight (delivery.getLoadWeight ()+ medicine.getWeight ());
        return deliveryRepository.save (delivery);
    }
    /**
     *
     * @param droneId drone id to for loaded medicines
     * @return A list of loaded medicines int the drone
     */
    @Override
    public List<DeliveryLoad> checkLoadedMedication(long droneId) {
        //check if drone exists
        Drone drone= droneRepository.findByIdAndSoftDeleteFalse (droneId).orElse (null);
        if(drone== null){
            throw new ItemNotFoundException ("Drone does not exist");
        }
        // check if drone is in load state
        if(!(drone.getState ()==DroneState.LOADING || drone.getState ()==DroneState.DELIVERING))
            throw new IllegalStateException ("Drone is not in loading or delivering state");
        return deliveryLoadRepository.checkLoadedMedicationOnDrone (droneId);
    }

    @Override
    public List<Drone> checkAvailableDrones(Pageable pageable) {
        return droneRepository.findAllByStateAndSoftDeleteFalse (DroneState.IDLE, pageable);
    }

    @Override
    public int checkDronePercentage(long droneId) {
        //check if drone exists
        Drone drone= droneRepository.findByIdAndSoftDeleteFalse (droneId).orElse (null);
        if(drone== null){
            throw new ItemNotFoundException ("Drone does not exist");
        }
        return drone.getBatteryPercentage ();
    }
}
