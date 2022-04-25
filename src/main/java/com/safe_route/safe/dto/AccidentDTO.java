package com.safe_route.safe.dto;

import com.safe_route.safe.model.AccidentModel;
import com.safe_route.safe.persistence.AccidentRepository;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccidentDTO {
    private int id;
    private Double lati;
    private Double longi;
    private int avgmeter;

    public AccidentDTO(final AccidentModel entity){
        this.id = entity.getId();
        this.avgmeter = entity.getAvgmeter();
        this.lati = entity.getLati();
        this.longi = entity.getLongi();
    }

    public static AccidentModel toEntity(final int id,final Double pLati, final Double pLongi,final int avgmeter){
        return AccidentModel.builder()
                .id(id)
                .avgmeter(avgmeter)
                .lati(pLati)
                .longi(pLongi)
                .build();
    }
}
