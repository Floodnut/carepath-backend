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
    private String type;
    private String name;
    private Double lati;
    private Double longti;

    public SafePointDTO(final SafePointModel entity){
        this.type = entity.getType();
        this.name = entity.getName();
        this.lati = entity.getLati();
        this.longti = entity.getLongti();
    }

    public static SafePointModel toEntity(final String type,final String name,final Double pLati, final Double pLongti){
        return SafePointModel.builder()
                .type(type)
                .name(name)
                .lati(pLati)
                .longti(pLongti)
                .build();
    }
}
