package com.safe_route.safe.dto;

import com.safe_route.safe.model.TmapTrafficModel;
import com.safe_route.safe.persistence.TmapTrafficRepository;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TmapTrafficDTO {
    private int congestion;
    private Double lat;
    private Double lon;

    public TmapTrafficDTO(final TmapTrafficModel entity){
        this.congestion = entity.getCongestion();
        this.lat = entity.getLat();
        this.lon = entity.getLon();
    }
}
