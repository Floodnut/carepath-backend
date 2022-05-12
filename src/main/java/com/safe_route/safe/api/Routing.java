package com.safe_route.safe.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Routing {
    
    public String getRoutePoint(String srcLati, String srcLongti, String dstLati, String dstLongti, int safeDegree) {
        try{
            String zm = new String();
            String op = new String();
            String conge = new String();
            if (safeDegree == 0){ /* safeDegree 0 */
                zm = "15";
                op = "10";
                conge = "3";
            }
            else if (safeDegree == 1){ /* safeDegree 1 */
                zm = "15";
                op = "4";
                conge = "3";
            }
            else{ /* safeDegree 2 */
                zm = "15";
                op = "0";
                conge = "2";
            }
            String URL = "http://127.0.0.1:9002/routing?srcLati=";
            String param = srcLati + "&srcLongti="+ srcLongti + "&dstLati="+ dstLati+ "&dstLongti="+ dstLongti + "&zoom="+zm+"&congestion="+conge + "&sop=" + op ;

            URL url = new URL(URL+param);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET"); 

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
            String inputLine; 
            StringBuffer response = new StringBuffer(); 

            while ((inputLine = in.readLine()) != null) { 
                response.append(inputLine); 
            } in.close(); 

            return "{ \"status\" : 200 , \"data\" :" + response.toString() +"}";
        }
        catch(MalformedURLException e){
            return  "{ \"status\" : 400, \"data\" : \"Invalid Data\" }";
        }
        catch(IOException e){
            return  "{ \"status\" : 400, \"data\" : \"Invalid Data\"}";
        }
        catch(Exception e){
            return  "{ \"status\" : 400, \"data\" : \"Invalid Data\"}";
        }
    }

    // public String pdRouting(Double srcLati, Double srcLongti, Double dstLati, Double dstLongti, String passList, String appKey){
    //     //long beforeTime = System.currentTimeMillis();
    //     try{
    //         String REQURL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json";
    //         String json;
    //         URL url = new URL(REQURL);

    //         // Post Body 
    //         Map<String,Object> params = new LinkedHashMap<>(); 
    //         params.put("startX", srcLongti);
    //         params.put("startY", srcLati);
    //         params.put("endX", dstLongti);
    //         params.put("endY", dstLati);
    //         params.put("reqCoordType", "WGS84GEO");
    //         params.put("resCoordType", "WGS84GEO");
    //         params.put("startName", "출발");
    //         params.put("endName", "도착");
    
    //         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    //         StringBuilder postData = new StringBuilder();
            
    //         for(Map.Entry<String,Object> param : params.entrySet()) {
    //             if(postData.length() != 0) postData.append('&');
    //             postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
    //             postData.append('=');
    //             postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    //         }
    //         byte[] postDataBytes = postData.toString().getBytes("UTF-8");
    
            
    //         conn.setRequestMethod("POST");
    //         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    //         conn.setRequestProperty("appKey", appKey);
    //         conn.setDoInput(true);
    //         conn.setDoOutput(true);
    //         conn.getOutputStream().write(postDataBytes); // POST 호출
    
    //         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    
    //         String inputLine;
    //         String result = new String();
    //         while((inputLine = in.readLine()) != null) { // response 출력
    //             result += inputLine;
    //         }
     
    //         in.close();
    //         //long afterTime = System.currentTimeMillis();
    //         json = result;
    //         return result;
    //     }
    //     catch(Exception e){
    //         return e.toString();
    //     }
    // } 
}