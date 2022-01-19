package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.safe_route.safe.persistence.CCTVPersistence;
import com.safe_route.safe.model.CctvPosModel;

import org.json.simple.*;

@Service
public class LocationService {

    @Autowired
    private CCTVPersistence cctvPersistence;

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
}
