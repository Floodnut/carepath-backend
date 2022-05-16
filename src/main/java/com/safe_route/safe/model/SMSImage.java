package com.safe_route.safe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.awt.Image;
import java.awt.image.BufferedImage;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@RedisHash(value = "smsgeo", timeToLive = 3600)
public class SMSImage {
    
    @Id
    private String key;
    private byte[] cachedImage;

    public SMSImage(String key, byte[] cachedImage){
        this.key = key;
        this.cachedImage = cachedImage;
    }

    public byte[] getCachedimage(){
        return this.cachedImage;
    }

    public String getKey(){
        return this.key;
    }
}
