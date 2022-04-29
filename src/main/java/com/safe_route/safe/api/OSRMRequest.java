package com.safe_route.safe.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;


@AllArgsConstructor
@NoArgsConstructor
public class OSRMRequest {
    private static final String reqUrl = "http://127.0.0.1:5000/route/v1/driving/";
    /* concat example
        127.105399,37.3595704;127.108081,37.363186
    */
    private String srcLong;
    private String srcLati;
    private String dstLong;
    private String dstLati;
    private static final String reqParam = "?overview=false&steps=true";

    //transient URLStreamHandler handler;

    public String osrmRequest() throws MalformedURLException{
        HttpURLConnection conn = null;
        String urlConcat = this.reqUrl + srcLati + ',' + srcLong +';' + dstLati + ',' + dstLong + this.reqParam;
        URL url = new URL(urlConcat);

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); 
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode(); 
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
            String inputLine; 
            StringBuffer response = new StringBuffer(); 
            while ((inputLine = in.readLine()) != null) { 
                response.append(inputLine); 
            } 
            in.close();
            //System.out.println(response.toString());
            return response.toString();

        } catch(Exception e){
            e.printStackTrace();
            return "{ \"status\" : \"ERROR\"}";
        }

        
    }

}
