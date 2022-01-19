package com.safe_route.safe.dto;

import com.safe_route.safe.model.CctvPosModel;
import com.safe_route.safe.persistence.CCTVPersistence;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CCTVPosDTO{
    private int count;
    private String local_code;
    private Double lati;
    private Double longti;

    public CCTVPosDTO(final CctvPosModel entity){
        this.count = entity.getCount();
        this.lati = entity.getLati();
        this.longti = entity.getLongti();
    }

    public static CctvPosModel toEntity(final Double pLati, final Double pLongti){
        return CctvPosModel.builder()
                .lati(pLati)
                .longti(pLongti)
                .build();
    }
}