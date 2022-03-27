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

            /* 테스트용 */
            String rr = new String();
            rr = "";
  
            /* CCTV, 경찰서, 보안등 / 교통량 */
            List<SafePointModel> safePoints = new ArrayList<SafePointModel>();
            List<TmapTrafficModel> tmapPoints = new ArrayList<TmapTrafficModel>();
            List<TrafficModel> trafficPoints = new ArrayList<TrafficModel>();

            /* AStat List */
            List<AStar> aStarOpen = new ArrayList<AStar>();
            List<AStar> aStarClosed = new ArrayList<AStar>();
            //AStar startNode = 
            //aStarClosed.add();
            
            /* 안전 노드 확인 */
            for (int i = 0 ; i < jarr.size(); i++){
                long m = 150;
                int maxidx = -1;
                JSONObject j3 = (JSONObject) jarr.get(i);
                Double lati = (Double)j3.get("la");
                Double longi = (Double)j3.get("lo");

                /* 안전 노드, 교통 혼잡 지역*/
                List<SafePosModel> safes = service.findSafePos(lati, longi, 100);
                List<TmapTrafficModel> tmapSafes = service.findTmapTrafficPos(lati, longi);
                List<TrafficModel> trafficSafes = service.findTrafficPos(lati, longi);

                if(tmapSafes.size() > 0){
                    for(int j = 0 ; j < tmapSafes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        TmapTrafficModel tmap = new TmapTrafficModel();
                        tmap = tmapSafes.get(j);

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
                        safes.add(tmp);
                    }
                }

                if(trafficSafes.size() > 0){
                    for(int j = 0 ; j < trafficSafes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        TrafficModel traff = new TrafficModel();
                        traff = trafficSafes.get(j);
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
                        safes.add(tmp);
                    }
                }

                if (safes.size() > 0){
                    for(int j = 0 ; j < safes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        tmp = safes.get(j);
                        long dis = getDistance(lati, longi, tmp.getLati(), tmp.getLongi());
                        boolean validNode = isValid(tmp.getLati(), tmp.getLongi(), lati - 0.005, longi - 0.005, lati + 0.005, longi + 0.005);
                        
                        if (m > dis){
                            if(tmp.getType() == 2){
                                m = dis;
                                maxidx = j;
                            }
                            else if(tmp.getType() == 3 && validNode){
                                m = dis;
                                maxidx = j;                                
                            }
                            else if(tmp.getType() == 1){
                                m = dis;
                                maxidx = j;                                
                            }
                            else if(tmp.getType() == 4){
                                m = dis;
                                maxidx = j;
                            }
                            else{
                                continue;
                            }
                        } 
                    }
                    if (maxidx != -1){
                        SafePosModel tmp2 = safes.get(maxidx);
                        rr += Double.toString(tmp2.getLongi()) +',' + Double.toString(tmp2.getLati()) +'_';
                        safePoints.add(SafePointDTO.toEntity(tmp2.getType(),tmp2.getName(),tmp2.getLati(),tmp2.getLongi(),lati,longi));
                        //aStarOpen.add();
                    }
                }   
            }

            //aStartClosed.add();
            safePoints.add(SafePointDTO.toEntity(0, rr,1.0,1.0,1.0,1.0));
            List<SafePointDTO> dtos = safePoints.stream().map(SafePointDTO::new).collect(Collectors.toList());
            ResponseDTO<SafePointDTO> response = ResponseDTO.<SafePointDTO>builder().
                                                        total(safePoints.size()).
                                                        data(dtos).
                                                        build();

            return ResponseEntity.ok().body(response);            
        }catch(Exception e){
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
}