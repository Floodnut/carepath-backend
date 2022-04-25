package com.safe_route.safe.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Image;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

public class SmsImageAPI {

    public BufferedImage getPickup(String lati, String longi, String key) {
        try{
            String URL = "https://apis.openapi.sk.com/tmap/staticMap?";
            String appKey = "appKey=" + key;
            String param = "&longitude="+ longi + "&latitude=" + lati + "&coordType=WGS84GEO&zoom=15&markers=" + longi + "," + lati + "&format=PNG&width=512&height=512";

            URL url = new URL(URL+ appKey + param);

            return ImageIO.read(url);
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
