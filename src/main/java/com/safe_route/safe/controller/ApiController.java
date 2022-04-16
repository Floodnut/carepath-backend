package com.safe_route.safe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;  
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import org.apache.commons.io.IOUtils;

import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.URLConnection;

import com.safe_route.safe.service.OSRMRequestService;
import com.safe_route.safe.api.SmsImageAPI;
import com.safe_route.safe.middleware.SHA256;


@RestController
@RequestMapping("sms")
public class ApiController {
    
    /* API Test */
    @GetMapping("api/")
    public String apiTest() {
        return "This is api page";
    }

    /* 마중요청 문자 보내기 */
    @GetMapping(value = "/pickup", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPickUpLocation(
        @RequestParam(required = true) String lati, 
        @RequestParam(required = true) String longi) throws IOException {

        String data = null;
        try{
            SHA256 sha256 = new SHA256();
            SmsImageAPI sms = new SmsImageAPI();

            data = sms.getPickup(lati, longi);

            // URL imageURL = new URL(data);
            
            // URLConnection urlConnection = new URL(data).openConnection();

            // urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            // urlConnection.getInputStream();

            // BufferedImage bi = ImageIO.read(imageURL);
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // ImageIO.write(bi, "png", baos);
            // return baos.toByteArray();
            //System.out.println(data.toString());
    
            // String hash = sha256.encrypt(lati + longi);
            
            InputStream in = new ByteArrayInputStream(data.getBytes());
            System.out.println(data);
            return IOUtils.toByteArray(in);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return data.getBytes();
    }

    /* OSRM Backend Request */
    @GetMapping("/osrm")
    public String safeNode (
        @RequestParam(value = "srcLong")String srcLong,
        @RequestParam(value = "srcLati")String srcLati,
        @RequestParam(value = "dstLong")String dstLong,
        @RequestParam(value = "dstLati")String dstLati) {
        
        OSRMRequestService route = new OSRMRequestService(srcLong, srcLati, dstLong, dstLati);
        String response;

        try{
            response = route.osrmRequest();
            JSONObject data = new JSONObject();
            JSONObject res = new JSONObject();
    
            data.put("srcLong", srcLong);
            data.put("srcLati", srcLati);
            data.put("dstLong", dstLong);
            data.put("dstLati", dstLati);
    
            res.put("response",data);
            res.put("status","200");
    
            String jsonResponse = res.toJSONString();
    
            return response;

        }catch(MalformedURLException e){
            e.printStackTrace();
            response = "{ \"status\" : \"error\" }";
            return response;
        } 
    }
}