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
    private int type;
    private String name;
    private Double lati;
    private Double longi;

    public SafePosDTO(final SafePosModel entity){
        this.type = entity.getType();
        this.name = entity.getName();
        this.lati = entity.getLati();
        this.longi = entity.getLongi();
    }

    public static SafePosModel toEntity(final int type,final String name,final Double pLati, final Double pLongti){
        return SafePosModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longi(pLongti)
                .build();
    }
}
