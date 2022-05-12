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
import java.util.LinkedList;
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
import com.safe_route.safe.dto.WZoneDTO;
import com.safe_route.safe.dto.AStar;
import com.safe_route.safe.model.SafePointModel;
import com.safe_route.safe.model.SafePosModel;
import com.safe_route.safe.model.TmapTrafficModel;
import com.safe_route.safe.model.TrafficModel;
import com.safe_route.safe.model.WZoneModel;
import com.safe_route.safe.service.LocationService;
import com.safe_route.safe.api.Routing;

@RestController
@RequestMapping("safe")
public class LocationController {

    @Autowired
    private LocationService service;

    private final static Logger LOG = Logger.getGlobal();

    /* 도로 폭에 따른 안전도 가중치 */
    private static Double geoBound = 0.002;
    private static Double bound = 0.01414;
    private static Double safeScale = 1.0;
    private static Double[] roadSafety = {0.3882, 0.9071, 1.0}; //avenue, road, narrow 

    /* 범죄율 정(+) 가중치 */
    private static Double nlZone = 0.15567056743; //유흥업소
    private static Double nlStdDen = 0.138; //유흥업소 밀도 기준

    /* 범죄율 부(-) 가중치 */
    private static Double[] nodeSafety = { }; //CCTV, 경찰관서, 교통량, 보안등, 편의점 
    private static Double apt = 1.0; //아파트 단지
    private static Double schoolW = 1.0; //학교

    /* AStar 경유지 탐색을 위한 집합 */
    private LinkedHashSet<AStar> open;
    private LinkedHashSet<AStar> closed;

    private AStar srcNode;    
    private AStar dstNode; 
    private int   nodeId;

    /* 안전 노드 조회 */
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
            return ResponseEntity.ok().body("Invalid Data");
        }
    }

    /* 안전 경로 조회 */
    @GetMapping("/routing")
    public ResponseEntity<?> defaultRouting(
        @RequestParam(required = true) String srcLati,
        @RequestParam(required = true) String srcLongti,
        @RequestParam(required = true) String dstLati,
        @RequestParam(required = true) String dstLongti,
        @RequestParam(required = true) int safeDegree
    ){
        try{
            Routing routing = new Routing();
            JSONParser parser = new JSONParser();
            String jsonStr = new String();

            Double sourceLati = Double.parseDouble(srcLati);
            Double sourceLongi = Double.parseDouble(srcLongti);
            Double destLati = Double.parseDouble(dstLati);
            Double destLongi = Double.parseDouble(dstLongti);

            AStar toClosed = new AStar();
            Double toClosedFScore = Double.POSITIVE_INFINITY;

            /*   테스트   */
            String rr = "_";
            LOG.setLevel(Level.INFO);

            /* API 요청 */
            jsonStr = routing.getRoutePoint(srcLati, srcLongti, dstLati, dstLongti, safeDegree);
            
            /* API 응답 파싱 */
            Object obj = parser.parse(jsonStr);
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject j2 = (JSONObject) jsonObj.get("data");
            JSONArray jarr = (JSONArray) j2.get("validNode");
            JSONArray maxmin = (JSONArray) j2.get("maxmin");
            JSONArray vNodeList = (JSONArray) j2.get("validNodeList");
            int vPointer = 0;
            int aStarLayer = 1;
            int trafficId = 100000;

            /* CCTV, 경찰서, 보안등 / 교통량 */
            List<SafePointModel> safePoints = new ArrayList<SafePointModel>();
            List<TmapTrafficModel> tmapPoints = new ArrayList<TmapTrafficModel>();
            List<TrafficModel> trafficPoints = new ArrayList<TrafficModel>();

            /* 범위 누적 지점 */
            List<SafePosModel> pointSafes = new ArrayList<SafePosModel>();

            /* 안전 노드 확인 */
            LinkedList<AStar> wayPointsList = new LinkedList<AStar>();
            initAStar(sourceLati, sourceLongi, destLati, destLongi);
            wayPointsList.add(srcNode);

            LinkedHashSet<SafePosModel> wayPoints = new LinkedHashSet<SafePosModel>();
            for (int i = 1 ; i < jarr.size() - 1; i++){
                long vLimit = (long)vNodeList.get(vPointer);
                Double lati = 0.0;
                Double longi = 0.0;
                Double lati_1 = 0.0;
                Double longi_1 = 0.0;
                JSONObject j3 = (JSONObject) jarr.get(i);
                JSONObject jBefore = (JSONObject) jarr.get(i-1);
                Double lastLati = sourceLati;
                Double lastLongi = sourceLongi;
                int lastId = 0;    

                lati_1 = (Double)jBefore.get("la");
                longi_1 = (Double)jBefore.get("lo");
                lati = (Double)j3.get("la");
                longi = (Double)j3.get("lo");
                
                if(i - 1 == 0)
                    continue;

                /* 안전 노드, 교통 혼잡 지역*/
                List<SafePosModel> safes = service.findSafePosInArea(lati_1, longi_1, lati, longi);
                List<TmapTrafficModel> tmapSafes = service.findTmapTrafficPosInArea(lati_1, longi_1, lati, longi);
                List<TrafficModel> trafficSafes = service.findTrafficPosInArea(lati_1, longi_1, lati, longi);

                /* 티맵 교통 정보 */
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
                            tmp.setRoad(tmap.getRoad());
                            tmp.setRoadtype(tmap.getRoadtype());
                            tmp.setLati(tmap.getLat());
                            tmp.setLongi(tmap.getLon());
                            wayPoints.add(tmp);
                        }
                    }
                }

                /* 국가교통정보센터 교통 정보 */
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
                            tmp.setRoad(traff.getRoad());
                            tmp.setRoadtype(traff.getRoadtype());
                            tmp.setLati(traff.getLat());
                            tmp.setLongi(traff.getLon());
                            wayPoints.add(tmp);
                        }
                    }
                }
                
                /* CCTV, 보안등, 편의점, 경찰서 */
                if(safes.size() > 0){
                    for(int j = 0 ; j < safes.size() ; j++){
                        SafePosModel tmp = safes.get(j);
                        if(boundaryEquation(tmp.getLati(), tmp.getLongi(), lati_1, longi_1, lati, longi)){
                            wayPoints.add(tmp);
                        } 
                    }
                }
                
                /* 경유지 선택 */
                if (i == vLimit - 1){
                    
                    /* LinkedHashSet -> List 변환 */
                    List<SafePosModel> list = wayPoints.stream().collect(Collectors.toList());

                    /* AStar 알고리즘 작업 */
                    for(int i2 = 0 ; i2 < list.size() ; i2++){
                        AStar tmpNode = setAStarNode(list.get(i2), destLati, destLongi, aStarLayer);

                        open.add(tmpNode);
                    }

                    Iterator openIter = open.iterator();
                    toClosed = new AStar();
                    toClosedFScore = Double.POSITIVE_INFINITY;
                    
                    while(openIter.hasNext()){
                        AStar toClosedCandidate = (AStar)openIter.next();
                        Double lastNodeDist = getDistance(toClosedCandidate.getLati(),toClosedCandidate.getLongi(),lastLati,lastLongi);

                        if(lastNodeDist < 50 && lastNodeDist >= 0){
                            continue;
                        }
            

                        if(toClosedCandidate.getLayer() == aStarLayer && toClosedCandidate.getFScore() < toClosedFScore){
                            toClosed = toClosedCandidate;
                            toClosedFScore = toClosedCandidate.getFScore();
                        }
                    }
                    if(open.size() > 0 && open.contains(toClosed)){
                        open.remove(toClosed);
                        lastLati = toClosed.getLati();
                        lastLongi = toClosed.getLongi();
                        closed.add(toClosed);
                    }

                    /* ================ */
                    aStarLayer += (wayPoints.size() > 0) ? 1 : 0;
                    vPointer += (vPointer + 1 == vNodeList.size()) ? 0 : 1;
                    vLimit = (long)vNodeList.get(vPointer);
                    wayPoints.clear();
                }  
            }
            
            List<AStar> closedIter = closed.stream().collect(Collectors.toList());
            for(int closedIdx = 1 ; closedIdx < closedIter.size(); closedIdx++){
                AStar closedNode = closedIter.get(closedIdx);

                if((closedIter.get(closedIdx - 1)).getLayer() == closedNode.getLayer() && (closedIter.get(closedIdx - 1)).getFScore() > closedNode.getFScore()){
                    safePoints.remove(closedIdx - 1);
                }
                rr += Double.toString(closedNode.getLongi()) + "," + Double.toString(closedNode.getLati()) + "_";
                safePoints.add(SafePointDTO.toEntity(closedNode.getType(),closedNode.getName(),closedNode.getLati(),closedNode.getLongi()));
            }
            
            safePoints.add(SafePointDTO.toEntity(-1,rr,0.0,0.0));
            /* 반환 형태로 변환 */ 
            List<SafePointDTO> dtos = safePoints.stream().map(SafePointDTO::new).collect(Collectors.toList());

            ResponseDTO<SafePointDTO> response = ResponseDTO.<SafePointDTO>builder().
                                                        total(safePoints.size()).
                                                        data(dtos).
                                                        build();

            return ResponseEntity.ok().body(response);            
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("Invalid Data");
        }
    }

    /* 좌표사이 거리 구하기 */
    public Double getDistance(Double srcLati, Double srcLongi, Double dstLati, Double dstLongi){
        int r = 6371000; //미터
        Double s1 = srcLati * 3.1415/180; 
        Double s2 = dstLati * 3.1415/180;
        Double th1 = (dstLati - srcLati) * 3.1415/180;
        Double th2 =  (dstLongi - srcLongi) * 3.1415/180;

        Double a = Math.sin(th1/2) * Math.sin(th1/2) + Math.cos(s1) * Math.cos(s2) * Math.sin(th2/2) * Math.sin(th2/2);
        
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return r * c;
    } 

    /* 유효 노드 판정 */
    private boolean isValid(Double lati, Double longi, Double sLati, Double sLongti, Double bLati, Double bLongti){

        if (lati >= sLati && lati <= bLati && longi >= sLongti && longi <= bLongti){
            return true;
        }else{
            return false;
        }
    }

    /* 방정식을 통해 유효한 후보 노드인지 판정 */
    private boolean boundaryEquation(Double lati, Double longi, Double sLati, Double sLongi, Double bLati, Double bLongi){
        
        if(sLongi == bLongi){
            if (longi <= (sLongi + bound ) && longi >= (sLongi - bound )){
                return true;
            }
        }
        else if(sLati == bLati){
            if (lati <= (sLati + bound) && lati >= (sLati - bound )){
                return true;
            }
        }
        else{
            Double stdSlope = (bLati - sLati) / (bLongi - sLongi);
            Double polynomialLow = stdSlope * (longi - sLongi) - bound + sLati;
            Double polynomialHigh = stdSlope * (longi - sLongi) + bound  + sLati;
            if( polynomialLow <= lati && polynomialHigh >= lati){
                return true;
            }  
        }
        return false;
    }

    /* AStar 초기화 */
    private void initAStar(Double srcLati, Double srcLongi, Double dstLati, Double dstLongi){
        this.open = new LinkedHashSet<AStar>();
        this.closed = new LinkedHashSet<AStar>();
        this.srcNode = new AStar();
        this.dstNode = new AStar();

        /* 출발지 노드 초기화 */
        srcNode.setId(0);
        srcNode.setLayer(0);
        srcNode.setLati(srcLati);
        srcNode.setLongi(srcLongi);
        srcNode.setGScore();
        srcNode.setHScore(getDistance(srcLati, srcLongi, dstLati, dstLongi));
        srcNode.setSScore();
        srcNode.setFScore();
        this.nodeId = 1;
        closed.add(srcNode);
        
        /* 목적지 노드 초기화 */
        dstNode.setLati(dstLati);
        dstNode.setLongi(dstLongi);
        dstNode.setHScore();
        dstNode.setSScore();  
    }

    /* AStar 노드 추가 및 연결 */
    private AStar setAStarNode(SafePosModel node, Double dstLati, Double dstLongi, int layer){
        AStar tmpNode = new AStar();
        int validParent = 0;

        Object[] aStarArr = closed.toArray();
        Double initFScore = Double.POSITIVE_INFINITY;

        for(int idx = 1; idx < closed.size() ; idx++){
            AStar layerTmp = (AStar)aStarArr[idx];
            /* 노드 Layer 비교 */
            if(layerTmp.getLayer() == layer - 1){
                initFScore = layerTmp.getFScore();
                validParent = idx;
            }
        }

        tmpNode.setId((int)Math.round((node.getLati() + node.getLongi()) % 1 * 10000000));
        tmpNode.setParent((AStar)aStarArr[validParent]);
        tmpNode.setType(node.getType());
        tmpNode.setName(node.getName());
        tmpNode.setLati(node.getLati());
        tmpNode.setLongi(node.getLongi());
        tmpNode.setLayer(layer);
        tmpNode.setGScore(tmpNode.getParent().getGScore() + getDistance(tmpNode.getParent().getLati(), tmpNode.getParent().getLongi(), node.getLati(), node.getLongi()));
        tmpNode.setHScore(getDistance(node.getLati(), node.getLongi(), dstLati, dstLongi));
        tmpNode.setSScore(calcSScore(node.getRoadtype(), node.getType(), node.getLati(), node.getLongi()));
        tmpNode.setFScore();
        
        this.nodeId += 1;

        return tmpNode;
    }

    private Double calcSScore(int roadType, int nodeType, Double lati, Double longi){
        try{

            Double safetyRate = 1.0;
            List<WZoneModel> wZones = service.findWZone(lati - geoBound , longi - geoBound, lati + geoBound, longi + geoBound);
            Double wZoneDensity = wZones.stream().mapToDouble(WZoneModel::getArea).sum() / 40000;
            Double wZoneSafety = (wZoneDensity > 1) ?  1 / nlStdDen : wZoneDensity / nlStdDen;

            return roadSafety[roadType-1] + (wZoneSafety * nlZone);
        }
        catch(Exception e){
            return roadSafety[2];
        }
    }
}