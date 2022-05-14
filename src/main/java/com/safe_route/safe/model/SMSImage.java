package com.safe_route.safe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.time.LocalDateTime;
import java.awt.image.BufferedImage;
import lombok.Data;

@Data
@RedisHash(value = "smsgeo", timeToLive = 3600)
public class SMSImage {
    
    @Id
    private String id;
    private BufferedImage cachedImage;
    private LocalDateTime createdAt;

    public SMSImage(String key, BufferedImage image){
        this.id = key;
        this.cachedImage = image;
        this.createdAt = LocalDateTime.now();
    }
}
