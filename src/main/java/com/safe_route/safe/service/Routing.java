package com.safe_route.safe.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;


public class Routing {
    private final static String REQURL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json";
    private String json;

    public String pdRouting(Double srcLati, Double srcLongti, Double dstLati, Double dstLongti, String passList, String appKey){
        //long beforeTime = System.currentTimeMillis();
        try{
            URL url = new URL(REQURL);

            /* Post Body */
            Map<String,Object> params = new LinkedHashMap<>(); 
            params.put("startX", srcLongti);
            params.put("startY", srcLati);
            params.put("endX", dstLongti);
            params.put("endY", dstLati);
            params.put("reqCoordType", "WGS84GEO");
            params.put("resCoordType", "WGS84GEO");
            params.put("startName", "출발");
            params.put("endName", "도착");
    
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            StringBuilder postData = new StringBuilder();
            
            for(Map.Entry<String,Object> param : params.entrySet()) {
                if(postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
    
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("appKey", appKey);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes); // POST 호출
    
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    
            String inputLine;
            String result = new String();
            while((inputLine = in.readLine()) != null) { // response 출력
                result += inputLine;
            }
     
            in.close();
            //long afterTime = System.currentTimeMillis();
            json = result;
            return result;
        }
        catch(Exception e){
            return e.toString();
        }
    }


}
