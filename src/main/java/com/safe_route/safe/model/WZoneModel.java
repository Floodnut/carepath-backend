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
@Table(name = "wzone")
public class WZoneModel {
    @Id
    private int id;

    @Column(name="tp")
    private int type;

    private int roadtype;

    @Column(name="nm")
    private String name;
    private Double lati;
    private Double longi;    
}
