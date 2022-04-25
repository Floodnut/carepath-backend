package com.safe_route.safe.function;

import com.safe_route.safe.function.AStar;
import com.safe_route.safe.model.SafePosModel;

import java.util.List;
import java.lang.Math;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @Data
public class FSet {

    /* 거리 정수로 구하기 */
    private Double getDistance2Double(Double srcLati, Double srcLongi, Double dstLati, Double dstLongi ){
        int r = 6371000; //미터
        Double s1 = srcLati * 3.1415/180; 
        Double s2 = dstLati * 3.1415/180;
        Double th1 = (dstLati - srcLati) * 3.1415/180;
        Double th2 =  (dstLongi - srcLongi) * 3.1415/180;

        Double a = Math.sin(th1/2) * Math.sin(th1/2) + Math.cos(s1/2) * Math.cos(s2/2) + Math.sin(th2/2) * Math.sin(th2/2);
        
        return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    } 

    /* 거리 소숫점까지 구하기 */
    private long getDistance2Long(Double lat1, Double lon1, Double lat2, Double lon2) {
        int R = 6371000; // 미터
        Double v1 = lat1 * Math.PI/180; 
        Double v2 = lat2 * Math.PI/180;
        Double tv = (lat2-lat1) * Math.PI/180;
        Double tl = (lon2-lon1) * Math.PI/180;
        
        Double a = Math.sin(tv/2) * Math.sin(tv/2) + Math.cos(v1) * Math.cos(v2) * Math.sin(tl/2) * Math.sin(tl/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        Double d = R * c;
    
        return Math.round(d);
    }

}
