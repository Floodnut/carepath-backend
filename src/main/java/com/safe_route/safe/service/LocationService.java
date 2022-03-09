package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.safe_route.safe.persistence.CCTVPersistence;
import com.safe_route.safe.persistence.PolicePersistence;
import com.safe_route.safe.persistence.SafePersistence;
import com.safe_route.safe.model.PolicePosModel;
import com.safe_route.safe.model.CctvPosModel;
import com.safe_route.safe.model.SafePosModel;

import org.json.simple.*;

@Service
public class LocationService {

    @Autowired
    private CCTVPersistence cctvPersistence;

    @Autowired
    private PolicePersistence policePersistence;

    @Autowired
    private SafePersistence safePersistence;

    public List<CctvPosModel> findCCTV2(CctvPosModel src, CctvPosModel dst){

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
        
        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(sLati, sLongti, bLati, bLongti);
    }

    
    public List<CctvPosModel> findCCTV(String srcLati, String srcLongti, String dstLati, String dstLongti){

        Double srcDLati = Double.parseDouble(srcLati);
        Double srcDLongti = Double.parseDouble(srcLongti);
        Double dstDLati = Double.parseDouble(dstLati);
        Double dstDLongti = Double.parseDouble(dstLati);

        Double sLati;
        Double bLati;
        Double sLongti;
        Double bLongti;

        if(srcDLati > dstDLati){
            bLati = srcDLati;
            sLati = dstDLati;
        } else{
            bLati = dstDLati;
            sLati = srcDLati;
        }
        if(srcDLongti > dstDLongti){
            bLongti = srcDLongti;
            sLongti = dstDLongti;
        } else{
            bLongti = dstDLongti;
            sLongti = srcDLongti;
        }
        
        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(sLati, sLongti, bLati, bLongti);
    }

    public List<PolicePosModel> findPolice(String srcLati, String srcLongti, String dstLati, String dstLongti){

        Double srcDLati = Double.parseDouble(srcLati);
        Double srcDLongti = Double.parseDouble(srcLongti);
        Double dstDLati = Double.parseDouble(dstLati);
        Double dstDLongti = Double.parseDouble(dstLati);

        Double sLati;
        Double bLati;
        Double sLongti;
        Double bLongti;

        if(srcDLati > dstDLati){
            bLati = srcDLati;
            sLati = dstDLati;
        } else{
            bLati = dstDLati;
            sLati = srcDLati;
        }
        if(srcDLongti > dstDLongti){
            bLongti = srcDLongti;
            sLongti = dstDLongti;
        } else{
            bLongti = dstDLongti;
            sLongti = srcDLongti;
        }
        
        return policePersistence.findPolicePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(sLati, sLongti, bLati, bLongti);
    }


    public List<CctvPosModel> findNodeCCTV(Double lati, Double longi,int interval){

        Double w = 0.001;

        return cctvPersistence.findCctvPosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }

    public List<PolicePosModel> findNodePolice(Double lati, Double longi,int interval){

        Double w = 0.001;

        return policePersistence.findPolicePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }
        

    
    public List<SafePosModel> findSafePos(Double lati, Double longi, int interval){
        Double w = 0.001;

        return safePersistence.findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(lati - w, longi - w, lati + w, longi + w);
    }
}
