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
@Table(name = "safeposim")
public class SafePosModel {
    @Id
    private int id;
    private int tp;
    private String nm;
    private Double lati;
    private Double longi;
    private String road;
    private int roadtype;
}
