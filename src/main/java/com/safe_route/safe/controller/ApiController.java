package com.safe_route.safe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

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
import java.awt.Image;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.URLConnection;

import com.safe_route.safe.service.OSRMRequestService;
import com.safe_route.safe.api.SmsImageAPI;
import com.safe_route.safe.middleware.SHA256;


@RestController
@RequestMapping("api")
@PropertySource("classpath:api.properties")
public class ApiController {

    @Autowired
    Environment env;

    @Value("${appkey}")
    private String key;
    
    /* API Test */
    @GetMapping("api/")
    public String apiTest() {
        return "This is api page";
    }

    /* 마중요청 문자 보내기 */
    @GetMapping(value = "/sms", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPickUpLocation(
        @RequestParam(required = true) String lati, 
        @RequestParam(required = true) String longi) throws IOException {
        try{
            SmsImageAPI sms = new SmsImageAPI();
            SHA256 sha256 = new SHA256();

            
            BufferedImage data = sms.getPickup(lati, longi, key);
            BufferedImage bi = data;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            return baos.toByteArray();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
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