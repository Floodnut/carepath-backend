package com.safe_route.safe.dto;

import com.safe_route.safe.model.TrafficModel;
import com.safe_route.safe.persistence.TrafficRepository;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrafficDTO {

    private int congestion;
    private Double lat;
    private Double lon;
    private String road;
    private int roadtype;
}