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
@Table(name = "cctvpos")
public class CctvPosModel {
    @Id
    private int id;
    private String localcode;
    private String local;
    private int count;
    private Double lati;
    private Double longti;
}
