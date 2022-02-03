package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.safe_route.safe.persistence.CCTVPersistence;
import com.safe_route.safe.persistence.PolicePersistence;
import com.safe_route.safe.model.PolicePosModel;
import com.safe_route.safe.model.CctvPosModel;

import org.json.simple.*;

@Service
public class LocationService {

    @Autowired
    private CCTVPersistence cctvPersistence;

    @Autowired
    private PolicePersistence policePersistence;

    public List<CctvPosModel> findCCTV(CctvPosModel src, CctvPosModel dst){

        Double sLati;
        Double bLati;
        Double sLongti;
        Double bLongti;

        if(src.getLati() > dst.getLati()){
            bLati = src.getLati();
            sLati = dst.getLati();
        } else{
            bLati = dst.getLati();
            sLati = src.getLati();
        }
        if(src.getLongti() > dst.getLongti()){
            bLongti = src.getLongti();
            sLongti = dst.getLongti();
        } else{
            bLongti = dst.getLongti();
            sLongti = src.getLongti();
        }
        
        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(sLati, sLongti, bLati, bLongti );
    }

    /*public List<CctvPosModel> findNodeCCTV(String lati, String longi,int interval){

        Double w = 0.001;
        //if(interval >=80 && interval <=120){ w = 0.001 }

        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan((Double.parseDouble(lati) - w), (Double.parseDouble(longi) - w), (Double.parseDouble(lati) + w), (Double.parseDouble(longi) + w));
    }*/
    public List<CctvPosModel> findNodeCCTV(Double lati, Double longi,int interval){

        Double w = 0.002;
        //if(interval >=80 && interval <=120){ w = 0.001 }

        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    public List<PolicePosModel> findNodePolice(Double lati, Double longi,int interval){

        Double w = 0.002;
        //if(interval >=80 && interval <=120){ w = 0.001 }

        return policePersistence.findPolicePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }
    /*
    public List<SafePointModel> findTraffic(Double lati,Double longti){

    }*/
}
