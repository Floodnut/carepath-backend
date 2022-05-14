package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.*;
import java.util.List;

import com.safe_route.safe.persistence.TmapTrafficRepository;
import com.safe_route.safe.persistence.TrafficRepository;
import com.safe_route.safe.persistence.SafePersistence;
import com.safe_route.safe.persistence.WZoneRepository;
import com.safe_route.safe.persistence.SZoneRepository;
import com.safe_route.safe.model.TmapTrafficModel;
import com.safe_route.safe.model.TrafficModel;
import com.safe_route.safe.model.SafePosModel;
import com.safe_route.safe.model.WZoneModel;
import com.safe_route.safe.model.SZoneModel;

@Service
public class LocationService {

    private static Double bound = 0.001414;
    private static Double w = 0.001;

    @Autowired
    private SafePersistence safePersistence;

    @Autowired
    private TmapTrafficRepository tmapTraffic;

    @Autowired
    private TrafficRepository traffic;

    @Autowired
    private WZoneRepository wZone;

    @Autowired
    private SZoneRepository sZone;

    /* 좌표 근처 교통정보 */
    public List<TrafficModel> findTrafficPos(Double lati, Double longi){
        return traffic.findTraffic(lati - w, longi - w, lati + w, longi + w);
    }
    
    /* 범위 내 교통정보 */
    public List<TrafficModel> findTrafficPosInArea(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return traffic.findTraffic(lati2 - bound, longi2 - bound, lati + bound, longi + bound);
        } 
        else if(lati <= lati2 && longi > longi2){
            return traffic.findTraffic(lati - bound, longi2 - bound, lati2 + bound, longi + bound);
        }
        else if(lati > lati2 && longi <= longi2){
            return traffic.findTraffic(lati2 - bound, longi - bound, lati + bound, longi2 + bound);
        }
        else if(lati < lati2 && longi < longi2){
            return traffic.findTraffic(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
        }
        else{
            return traffic.findTraffic(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
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
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2 - bound, longi2 - bound, lati + bound, longi + bound);
        } 
        else if(lati <= lati2 && longi > longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati - bound, longi2 - bound, lati2 + bound, longi + bound);
        }
        else if(lati > lati2 && longi <= longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati2 - bound, longi - bound, lati + bound, longi2 + bound);
        }
        else if(lati < lati2 && longi < longi2){
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
        }
        else{
            return tmapTraffic.findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
        }
    }

    /* 좌표 근처 안전 노드 */
    public List<SafePosModel> findSafePos(Double lati, Double longi, int interval){
        Double w = 0.01;
        return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    /* 범위 내 안전 노드 */
    public List<SafePosModel> findSafePosInArea(Double lati, Double longi, Double lati2, Double longi2){
        if (lati > lati2 && longi > longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati2 - bound, longi2 - bound, lati + bound, longi + bound);
        } 
        else if(lati <= lati2 && longi > longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - bound, longi2 - bound, lati2 + bound, longi + bound);
        }
        else if(lati > lati2 && longi <= longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati2 - bound, longi - bound, lati + bound, longi2 + bound);
        }
        else if(lati < lati2 && longi < longi2){
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
        }
        else{
            return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - bound, longi - bound, lati2 + bound, longi2 + bound);
        }
    }

    /* 위험 노드 */
    public List<WZoneModel> findWZone(Double lati, Double longi, Double lati2, Double longi2){
        return wZone.findAllWZone(lati, longi, lati2, longi2);
    }

    /* 안전 노드 */
    public List<SZoneModel> findSZone(Double lati, Double longi, Double lati2, Double longi2){
        return sZone.findAllSZone(lati, longi, lati2, longi2);
    }
}
