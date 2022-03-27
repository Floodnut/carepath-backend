package com.safe_route.safe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "traffic")
public class TrafficModel {

    @Id
    private int id;
    private int congestion;
    private Double lat;
    private Double lon;
}
