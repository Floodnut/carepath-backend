package com.safe_route.safe.dto;

import com.safe_route.safe.model.SafePointModel;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SafePointDTO {
    private int type;
    private String name;
    private Double lati;
    private Double longti;
    private Double oriLati;
    private Double oriLongti;

    public SafePointDTO(final SafePointModel entity){
        this.type = entity.getType();
        this.name = entity.getName();
        this.lati = entity.getLati();
        this.longti = entity.getLongti();
        this.oriLati = entity.getOriLati();
        this.oriLongti = entity.getOriLongti();
    }

    public static SafePointModel toEntity(final int type,final String name,final Double pLati, final Double pLongti,final Double oriLati, final Double oriLongti){
        return SafePointModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longti(pLongti)
                .oriLati(oriLati)
                .oriLongti(oriLongti)
                .build();
    }

    public static SafePointModel toEntity(final int type,final String name,final Double pLati, final Double pLongti){
        return SafePointModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longti(pLongti)
                .build();
    }
}
