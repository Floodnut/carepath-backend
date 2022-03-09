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
import com.safe_route.safe.dto.CCTVPosDTO;
import com.safe_route.safe.dto.PolicePosDTO;
import com.safe_route.safe.dto.ResponseDTO;
import com.safe_route.safe.model.SafePointModel;
import com.safe_route.safe.model.SafePosModel;
import com.safe_route.safe.model.CctvPosModel;
import com.safe_route.safe.model.PolicePosModel;
import com.safe_route.safe.service.LocationService;
import com.safe_route.safe.service.Routing;

@RestController
@RequestMapping("safe")
public class LocationController {

    private String APPKEY = "";
    private final static Logger LOG = Logger.getGlobal();

    @Autowired
    private LocationService service;

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
            String jsonStr = routing.getRoutePoint(srcLati, srcLongti, dstLati, dstLongti,"");
            Object obj = parser.parse(jsonStr);
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject j2 = (JSONObject) jsonObj.get("data");
            JSONArray jarr = (JSONArray) j2.get("validNode");

            List<SafePointModel> safePoints = new ArrayList<SafePointModel>();

            for (int i = 0 ; i < jarr.size(); i++){
                long max = 0;
                int maxidx = 0;
                
                JSONObject j3 = (JSONObject) jarr.get(i);
                Double lati = (Double)j3.get("la");
                Double longi = (Double)j3.get("lo");

                List<SafePosModel> safes = service.findSafePos(lati, longi, 100);

                if (safes.size() > 0){
                    for(int j = 0 ; j < safes.size();j++){
                        SafePosModel tmp = new SafePosModel();
                        tmp = safes.get(j);
                        long dis = getDistance(lati,longi,tmp.getLati(),tmp.getLongi());
                        if (max < dis){
                            max = dis;
                            maxidx = j;
                        }     
                    }
                    SafePosModel tmp2 = safes.get(maxidx);
                    safePoints.add(SafePointDTO.toEntity(tmp2.getType(),tmp2.getName(),tmp2.getLati(),tmp2.getLongi(),lati,longi));
                }
            }


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
}