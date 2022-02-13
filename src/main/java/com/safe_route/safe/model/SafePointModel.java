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
public class SafePointModel {
    @Id
    private int id;
    private String type;
    private String name;
    private Double lati;
    private Double longti;
    private Double oriLati;
    private Double oriLongti;
}
