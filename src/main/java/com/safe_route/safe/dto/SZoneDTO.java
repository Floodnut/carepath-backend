package com.safe_route.safe.dto;

import com.safe_route.safe.model.SZoneModel;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SZoneDTO {
    private int type;
    private String name;
    private Double lati;
    private Double longi;
    private String addr;

    public SZoneDTO(final SZoneModel entity){
        this.type = entity.getType();
        this.name = entity.getName();
        this.lati = entity.getLati();
        this.longi = entity.getLongi();
        this.addr = entity.getAddr();
    }

    public static SZoneModel toEntity(final int type, final String name,final Double pLati, final Double pLongi,final String _addr){
        return SZoneModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longi(pLongi)
                .addr(_addr)
                .build();
    }
}