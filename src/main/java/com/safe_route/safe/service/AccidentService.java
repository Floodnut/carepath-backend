package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.safe_route.safe.persistence.AccidentRepository;
import com.safe_route.safe.model.AccidentModel;

@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accident;

    /* 현재 위치 근처 보행자 사고 다발 지역 */
    public List<AccidentModel> findAccident(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return accident.findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2, longi2, lati, longi);
        } 
        else if(lati <= lati2 && longi > longi2){
            return accident.findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi2, lati2, longi);
        }
        else if(lati > lati2 && longi <= longi2){
            return accident.findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2, longi, lati, longi2);
        }
        else if(lati < lati2 && longi < longi2){
            return accident.findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi, lati2, longi2);
        }
        else{
            return accident.findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi, lati2, longi2);
        }
    }
}
