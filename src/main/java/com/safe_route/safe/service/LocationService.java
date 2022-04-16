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

    /* 좌표 근처 교통정보 */
    public List<TrafficModel> findTrafficPos(Double lati, Double longi){
        Double w = 0.001;
        return traffic.findTraffic(lati - w, longi - w, lati + w, longi + w);
    }
    
    /* 범위 내 교통정보 */
    public List<TrafficModel> findTrafficPosInArea(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return traffic.findTraffic(lati2, longi2, lati, longi);
        } 
        else if(lati <= lati2 && longi > longi2){
            return traffic.findTraffic(lati, longi2, lati2, longi);
        }
        else if(lati > lati2 && longi <= longi2){
            return traffic.findTraffic(lati2, longi, lati, longi2);
        }
        else if(lati < lati2 && longi < longi2){
            return traffic.findTraffic(lati, longi, lati2, longi2);
        }
        else{
            return traffic.findTraffic(lati, longi, lati2, longi2);
        }
    }
    
    /* 좌표 근처 티맵 교통정보 */
    public List<TmapTrafficModel> findTmapTrafficPos(Double lati, Double longi){
        Double w = 0.001;
        return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    /* 범위 내 티맵 교통정보 */
    public List<TmapTrafficModel> findTmapTrafficPosInArea(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2, longi2, lati, longi);
        } 
        else if(lati <= lati2 && longi > longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi2, lati2, longi);
        }
        else if(lati > lati2 && longi <= longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2, longi, lati, longi2);
        }
        else if(lati < lati2 && longi < longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi, lati2, longi2);
        }
        else{
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati, longi, lati2, longi2);
        }
    }

    /* 좌표 근처 안전 노드 */
    public List<SafePosModel> findSafePos(Double lati, Double longi, int interval){
        Double w = 0.001;
        return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    /* 범위 내 안전 노드 */
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
