package com.musalasoft.droneservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musalasoft.droneservice.common.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@Entity
@Table(name="tb_delivery_load")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryLoad extends BaseEntity {
    @ManyToOne
    private Medicine medicine;
    private int count;
    @ManyToOne
    @JsonIgnore
    private  Delivery delivery;

}
