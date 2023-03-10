package com.musalasoft.droneservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musalasoft.droneservice.common.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
@Entity
@Table(name="tb_medicine")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Medicine extends BaseEntity {
    @NotBlank(message = "Name cannot be null")
    @Pattern (regexp = "[_A-Z0-9a-z]+(_[A-Za-z0-9]+)*$")
    private String name;
    private double weight;
    @Pattern(regexp = "[_A-Z0-9]+(_[A-Z]+)*$")
    private String code;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image;
}
