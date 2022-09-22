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
import org.springframework.core.GenericTypeResolver;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;  
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import org.apache.commons.io.IOUtils;

import java.security.NoSuchAlgorithmException;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.Optional;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import com.safe_route.safe.api.OSRMRequest;
import com.safe_route.safe.api.SmsImageAPI;
import com.safe_route.safe.model.SMSImage;
import com.safe_route.safe.middleware.SHA256;
import com.safe_route.safe.service.SMSRedisService;
import com.safe_route.safe.persistence.SMSImageRedisRepository;


@RestController
@RequestMapping("api")
@PropertySource("classpath:api.properties")
public class ApiController {

    @Autowired
    Environment env;

    @Autowired
    private SMSRedisService smsRedisService;

    @Autowired
    private SMSImageRedisRepository smsRedisRepo;

    @Value("${appkey}")
    private String appkey;

    private SHA256 hashing = new SHA256();
    



    /* 마중요청 문자 보내기 */
    @GetMapping(value = "/sms", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getPickUpLocation(
        @RequestParam(required = true) String lati, 
        @RequestParam(required = true) String longi) throws IOException, NoSuchAlgorithmException {
        try{
            
            String key = hashing.encrypt(lati + longi);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            /* Redis Retrieve */
            Optional<SMSImage> opt = smsRedisRepo.findById(key);
            SMSImage cachedImage = opt.get();

            return cachedImage.getCachedimage();
        }catch(NullPointerException ne){
            String key = hashing.encrypt(lati + longi);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SmsImageAPI sms = new SmsImageAPI();
            BufferedImage bi = sms.getPickup(lati, longi, appkey);
            Image image = bi;
            ImageIO.write(bi, "png", baos);

            /* Redis Caching */
            SMSImage cache = new SMSImage(key,  baos.toByteArray());
            smsRedisRepo.save(cache);

            /* Redis Value Return */
            return baos.toByteArray();
        }catch(NoSuchElementException ee){
            
            String key = hashing.encrypt(lati + longi);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SmsImageAPI sms = new SmsImageAPI();
            BufferedImage bi = sms.getPickup(lati, longi, appkey);
            Image image = bi;
            ImageIO.write(bi, "png", baos);

            /* Redis Caching */
            SMSImage cache = new SMSImage(key,  baos.toByteArray());
            smsRedisRepo.save(cache);

            /* Redis Value Return */
            return baos.toByteArray();
        }
        catch(NoSuchAlgorithmException ae){
            //ae.printStackTrace();
            return null;
        }
        catch(Exception e){
            //e.printStackTrace();
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
        
        OSRMRequest route = new OSRMRequest(srcLong, srcLati, dstLong, dstLati);
        String response;

        try{
            response = route.osrmRequest();
            JSONObject data = new JSONObject();
            JSONObject res = new JSONObject();
    
            data.put("srcLong", srcLong);
            data.put("srcLati", srcLati);
            data.put("dstLong", dstLong);
            data.put("dstLati", dstLati);
    
            res.put("response", data);
            res.put("status", "200");
    
            String jsonResponse = res.toJSONString();
    
            return response;

        }catch(MalformedURLException e){
            //e.printStackTrace();
            return "{ \"status\" : \"error\" }";
        } 
        catch(Exception e){
            //e.printStackTrace();
            return "{ \"status\" : \"error\" }";
        } 
    }
}