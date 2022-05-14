package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class SMSRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setRedisImage(String lati, String longi, BufferedImage image){
        String key = lati.substring(0, 9) + longi.substring(0, 10);

        SetOperations<String, Object> value = redisTemplate.opsForSet();
        value.add(key, image);
    }
}
