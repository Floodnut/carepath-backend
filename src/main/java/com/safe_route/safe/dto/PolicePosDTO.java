package com.safe_route.safe.dto;

import com.safe_route.safe.model.PolicePosModel;
import com.safe_route.safe.persistence.PolicePersistence;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PolicePosDTO {
    private String police;
    private String policesub;
    private String policetype;
    private Double lati;
    private Double longti;

    public PolicePosDTO(final PolicePosModel entity){
        this.police = entity.getPolice();
        this.policesub = entity.getPolicesub();
        this.policetype = entity.getPolicetype();
        this.lati = entity.getLati();
        this.longti = entity.getLongti();
    }

    public static PolicePosModel toEntity(final Double pLati, final Double pLongti){
        return PolicePosModel.builder()
                .lati(pLati)
                .longti(pLongti)
                .build();
    }
}
