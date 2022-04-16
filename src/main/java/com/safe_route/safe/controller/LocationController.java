package com.safe_route.safe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;  
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.Math;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.safe_route.safe.dto.SafePointDTO;
import com.safe_route.safe.dto.SafePosDTO;
import com.safe_route.safe.dto.TmapTrafficDTO;
import com.safe_route.safe.dto.TrafficDTO;
import com.safe_route.safe.dto.ResponseDTO;
import com.safe_route.safe.dto.AStar;
import com.safe_route.safe.model.SafePointModel;
import com.safe_route.safe.model.SafePosModel;
import com.safe_route.safe.model.TmapTrafficModel;
import com.safe_route.safe.model.TrafficModel;
import com.safe_route.safe.service.LocationService;
import com.safe_route.safe.service.Routing;

@RestController
@RequestMapping("safe")
public class LocationController {

    private String APPKEY = "";
    private final static Logger LOG = Logger.getGlobal();

    @Autowired
    private LocationService service;

    @GetMapping("/node")
    public ResponseEntity<?> findNode(
        @RequestParam(required = true) int mode,
        @RequestParam(required = true) String srcLati,
        @RequestParam(required = true) String srcLongti,
        @RequestParam(required = false) String dstLati,
        @RequestParam(required = false) String dstLongti
    ){
        try{
            Double lati = Double.parseDouble(srcLati);
            Double longi = Double.parseDouble(srcLongti);
            Double lati2 = 0.0;
            Double longi2 = 0.0;
            List<SafePosModel> safes = new ArrayList<SafePosModel>();

            /* 현재 위치 기준 */   
            if(mode == 1){              
                safes = service.findSafePos(lati, longi, 100);
            }
            /* 지정한 범위 기준 */   
            else if(mode == 2){
                lati2 = Double.parseDouble(dstLati);
                longi2 = Double.parseDouble(dstLongti);
                safes = service.findSafePosInArea(lati, longi, lati2, longi2);
            }

            List<SafePosDTO> dtos = safes.stream().map(SafePosDTO::new).collect(Collectors.toList());
            ResponseDTO<SafePosDTO> response = ResponseDTO.<SafePosDTO>builder().
                                                        total(safes.size()).
                                                        data(dtos).
                                                        build();
            return ResponseEntity.ok().body(response);  
        }catch(Exception e){
            return ResponseEntity.ok().body(e.toString());
        }
    }

    @GetMapping("/routing")
    public ResponseEntity<?> defaultRouting(
        @RequestParam(required = true) String srcLati,
        @RequestParam(required = true) String srcLongti,
        @RequestParam(required = true) String dstLati,
        @RequestParam(required = true) String dstLongti,
        @RequestParam(required = true) int safeDegree
    ){
        try{
            Double sourceLati = Double.parseDouble(srcLati);
            Double sourceLongi = Double.parseDouble(srcLongti);
            Double destLati = Double.parseDouble(dstLati);
            Double destLongi = Double.parseDouble(dstLongti);

            LOG.setLevel(Level.INFO);
            Routing routing = new Routing();
            JSONParser parser = new JSONParser();
            String jsonStr = new String();
            /* API 요청 */
            if (safeDegree == 0){
                jsonStr = routing.getRoutePoint(srcLati, srcLongti, dstLati, dstLongti,"","4");
            }
            else if (safeDegree == 1){
                jsonStr = routing.getRoutePoint(srcLati, srcLongti, dstLati, dstLongti,"","3");
            }
            else if (safeDegree == 2){
                jsonStr = routing.getRoutePoint(srcLati, srcLongti, dstLati, dstLongti,"","2");
            }
            /* API 응답 파싱 */
            Object obj = parser.parse(jsonStr);
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject j2 = (JSONObject) jsonObj.get("data");
            JSONArray jarr = (JSONArray) j2.get("validNode");
            JSONArray maxmin = (JSONArray) j2.get("maxmin");
            JSONArray vNodeList = (JSONArray) j2.get("validNodeList");
            int vPointer = 0; 

            /*   테스트   */
            String rr = "_";
            /*          */
  
            /* CCTV, 경찰서, 보안등 / 교통량 */
            List<SafePointModel> safePoints = new ArrayList<SafePointModel>();
            List<TmapTrafficModel> tmapPoints = new ArrayList<TmapTrafficModel>();
            List<TrafficModel> trafficPoints = new ArrayList<TrafficModel>();

            /* 범위 누적 지점 */
            List<SafePosModel> pointSafes = new ArrayList<SafePosModel>();

            /* AStat List */
            List<AStar> aStarOpen = new ArrayList<AStar>();
            List<AStar> aStarClosed = new ArrayList<AStar>();
            //AStar startNode = 
            //aStarClosed.add();
            
            /* 안전 노드 확인 */
            LinkedHashSet<SafePosModel> wayPoints = new LinkedHashSet<SafePosModel>();
            for (int i = 1 ; i < jarr.size(); i++){
                long m = 150;
                int maxidx = -1;
                long vLimit = (long)vNodeList.get(vPointer);
                Double lati = 0.0;
                Double longi = 0.0;
                Double lati_1 = 0.0;
                Double longi_1 = 0.0;
                JSONObject j3 = (JSONObject) jarr.get(i);
                JSONObject jBefore = (JSONObject) jarr.get(i-1);      

                lati_1 = (Double)jBefore.get("la");
                longi_1 = (Double)jBefore.get("lo");
                lati = (Double)j3.get("la");
                longi = (Double)j3.get("lo");
                

                /* 안전 노드, 교통 혼잡 지역*/
                // List<SafePosModel> safes = service.findSafePos(lati, longi, 100);
                // List<TmapTrafficModel> tmapSafes = service.findTmapTrafficPos(lati, longi);
                // List<TrafficModel> trafficSafes = service.findTrafficPos(lati, longi);

                List<SafePosModel> safes = service.findSafePosInArea(lati_1, longi_1, lati, longi);
                List<TmapTrafficModel> tmapSafes = service.findTmapTrafficPosInArea(lati_1, longi_1, lati, longi);
                List<TrafficModel> trafficSafes = service.findTrafficPosInArea(lati_1, longi_1, lati, longi);

                if(tmapSafes.size() > 0){
                    for(int j = 0 ; j < tmapSafes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        TmapTrafficModel tmap = tmapSafes.get(j);
                        if(boundaryEquation(tmap.getLat(), tmap.getLon(), lati_1, longi_1, lati, longi)){
                            int con = tmap.getCongestion();
                            tmp.setType(3);
                            if (con == 2){
                                tmp.setName("서행");
                            }
                            else if (con == 3){
                                tmp.setName("정체");
                            }
                            else if (con == 4){
                                tmp.setName("혼잡");
                            }
                            tmp.setLati(tmap.getLat());
                            tmp.setLongi(tmap.getLon());
                            wayPoints.add(tmp);
                        }
                    }
                }

                if(trafficSafes.size() > 0){
                    for(int j = 0 ; j < trafficSafes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        TrafficModel traff = trafficSafes.get(j);
                        if(boundaryEquation(traff.getLat(), traff.getLon(), lati_1, longi_1, lati, longi)){
                            int con = traff.getCongestion();
                            tmp.setType(3);
                            if (con == 2){
                                tmp.setName("서행");
                            }
                            else if (con == 3){
                                tmp.setName("정체");
                            }
                            else if (con == 4){
                                tmp.setName("혼잡");
                            }
                            tmp.setLati(traff.getLat());
                            tmp.setLongi(traff.getLon());
                            wayPoints.add(tmp);
                        }
                    }
                }
                
                if (safes.size() > 0 ){
                    for(int j = 0 ; j < safes.size();j++){
                        SafePosModel tmp = safes.get(j);
                        if(boundaryEquation(tmp.getLati(), tmp.getLongi(), lati_1, longi_1, lati, longi)){
                            wayPoints.add(tmp);
                        } 
                    }
                } 

                if (i == vLimit || i == jarr.size() - 1 ){
                    if (vPointer == vNodeList.size() - 1){
                        vLimit = jarr.size() - 1;
                    }
                    else{
                        vPointer += 1;
                    }
                    List<SafePosModel> list = wayPoints.stream().collect(Collectors.toList());
                    for(int midx = 0 ; midx < list.size() ; midx++){
                        SafePosModel tmp = list.get(midx);
                        long dis = 0;
                        dis = getDistance(lati, longi, tmp.getLati(), tmp.getLongi());
                            if(tmp.getType() == 2){
                                m = dis;
                                maxidx = midx;
                            }
                            else if(tmp.getType() == 3){
                                m = dis;
                                maxidx = midx;                                
                            }
                            else if(tmp.getType() == 1){
                                m = dis;
                                maxidx = midx;                                
                            }
                            else if(tmp.getType() == 4){
                                m = dis;
                                maxidx = midx;
                            }
                            else if(tmp.getType() == 5){
                                m = dis;
                                maxidx = midx;
                            }
                            else{
                                continue;
                            }
                    }
                    if (maxidx != -1){
                        SafePosModel tmp2 = list.get(maxidx);
                        // rr += Double.toString(tmp2.getLongi()) +',' + Double.toString(tmp2.getLati()) +'_';
                        safePoints.add(SafePointDTO.toEntity(tmp2.getType(),tmp2.getName(),tmp2.getLati(),tmp2.getLongi(),lati,longi));
                        //aStarOpen.add();
                    } 
                    wayPoints.clear();
                }   
            }

            //aStartClosed.add();
            //safePoints.add(SafePointDTO.toEntity(0, rr,1.0,1.0,1.0,1.0));
            List<SafePointDTO> dtos = safePoints.stream().map(SafePointDTO::new).collect(Collectors.toList());
            ResponseDTO<SafePointDTO> response = ResponseDTO.<SafePointDTO>builder().
                                                        total(safePoints.size()).
                                                        data(dtos).
                                                        build();

            return ResponseEntity.ok().body(response);            
        }
        catch(Exception e){
            //LOG.info(e.toString());
            return ResponseEntity.ok().body(e.toString());
        }
    }

    private long getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        int R = 6371000; // metres
        Double v1 = lat1 * Math.PI/180; // v, l in radians
        Double v2 = lat2 * Math.PI/180;
        Double tv = (lat2-lat1) * Math.PI/180;
        Double tl = (lon2-lon1) * Math.PI/180;
        
        Double a = Math.sin(tv/2) * Math.sin(tv/2) + Math.cos(v1) * Math.cos(v2) * Math.sin(tl/2) * Math.sin(tl/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        Double d = R * c;
    
        return Math.round(d);
    }

    private boolean isValid(Double lati, Double longi, Double sLati, Double sLongti, Double bLati, Double bLongti){

        if (lati >= sLati && lati <= bLati && longi >= sLongti && longi <= bLongti){
            return true;
        }else{
            return false;
        }
    }

    private boolean boundaryEquation(Double lati, Double longi, Double sLati, Double sLongi, Double bLati, Double bLongi){
        if(sLongi == bLongi){
            if (longi <= (sLongi + 0.01) && longi >= (sLongi - 0.01)){
                return true;
            }
            else{
                return false;
            }
        }
        else if(sLati == bLati){
            if (lati <= (sLati + 0.01) && lati >= (sLati - 0.01)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            Double stdSlope = (bLati - sLati) / (bLongi - sLongi);
            Double polynomialLow = stdSlope * (longi - sLongi) - 0.01414 + sLati;
            Double polynomialHigh = stdSlope * (longi - sLongi) + 0.01414 + sLati;
            if( polynomialLow <= lati && polynomialHigh >= lati){
                return true;
            }
            else{
                return false;
            }
        }
        // else if(stdSlope < 0){
        //     if( (stdSlope * (longi) - 0.01414) <= lati && (stdSlope * (longi) + 0.01414) <= lati){
        //         return true;
        //     }
        //     else{
        //         return false;
        //     }
        // }
    }
}