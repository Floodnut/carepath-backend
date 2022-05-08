package com.safe_route.safe.dto;

import com.safe_route.safe.model.WZoneModel;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WZoneDTO {
    private int type;
    private String name;
    private Double lati;
    private Double longi;
    private String road;
    private int roadtype;

    public WZoneDTO(final WZoneModel entity){
        this.type = entity.getType();
        this.name = entity.getName();
        this.lati = entity.getLati();
        this.longi = entity.getLongi();
        this.roadtype = entity.getRoadtype();
    }

    public static WZoneModel toEntity(final int type, final int roadTypeValue, final String name,final Double pLati, final Double pLongi){
        return WZoneModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longi(pLongi)
                .roadtype(roadTypeValue)
                .build();
    }
}