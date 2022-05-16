package com.safe_route.safe.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import java.awt.Image;

import com.safe_route.safe.model.SMSImage;

@Service
public class SMSRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setRedisImage(SMSImage image){

        SetOperations<String, Object> value = redisTemplate.opsForSet();
        value.add(image.getKey(), image.getCachedimage());
    }
}
