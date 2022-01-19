package com.safe_route.safe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "police_pos")
public class PolicePosModel {
    @Id
    private int id;
    private String police;
    private String policesub;
    private String policetype;
    private Double lati;
    private Double longti;
}
