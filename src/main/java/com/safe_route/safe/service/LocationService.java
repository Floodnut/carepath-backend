package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.*;
import java.util.List;

import com.safe_route.safe.persistence.TmapTrafficRepository;
import com.safe_route.safe.persistence.TrafficRepository;
import com.safe_route.safe.persistence.SafePersistence;
import com.safe_route.safe.model.TmapTrafficModel;
import com.safe_route.safe.model.TrafficModel;
import com.safe_route.safe.model.SafePosModel;

@Service
public class LocationService {

    @Autowired
    private SafePersistence safePersistence;

    @Autowired
    private TmapTrafficRepository tmapTraffic;

    @Autowired
    private TrafficRepository traffic;

    public List<TrafficModel> findTrafficPos(Double lati, Double longi){
        Double w = 0.001;
        return traffic.findTraffic(lati - w, longi - w, lati + w, longi + w);
    }
    
    public List<TmapTrafficModel> findTmapTrafficPos(Double lati, Double longi){
        Double w = 0.001;
        return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    public List<SafePosModel> findSafePos(Double lati, Double longi, int interval){
        Double w = 0.001;
        return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    public List<SafePosModel> findSafePosInArea(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati2, longi2, lati, longi);
        } 
        else if(lati <= lati2 && longi > longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati, longi2, lati2, longi);
        }
        else if(lati > lati2 && longi <= longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati2, longi, lati, longi2);
        }
        else if(lati < lati2 && longi < longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati, longi, lati2, longi2);
        }
        else{
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati, longi, lati2, longi2);
        }
    }
}
