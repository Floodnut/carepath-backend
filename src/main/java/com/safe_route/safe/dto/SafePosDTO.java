package com.safe_route.safe.dto;

import com.safe_route.safe.model.SafePosModel;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SafePosDTO {
    private int tp;
    private String nm;
    private Double lati;
    private Double longi;
    private String road;
    private int roadtype;

    public SafePosDTO(final SafePosModel entity){
        this.tp = entity.getTp();
        this.nm = entity.getNm();
        this.lati = entity.getLati();
        this.longi = entity.getLongi();
        this.road = entity.getRoad();
        this.roadtype = entity.getRoadtype();
    }

    public static SafePosModel toEntity(final int type,final String name,final Double pLati, final Double pLongti, final String roadName, final int roadTypeValue){
        return SafePosModel.builder()
                .tp(type)
                .nm(name)
                .lati(pLati)
                .longi(pLongti)
                .road(roadName)
                .roadtype(roadTypeValue)
                .build();
    }
}
