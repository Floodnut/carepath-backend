package com.safe_route.safe.persistence;

import org.springframework.data.repository.CrudRepository;
import com.safe_route.safe.model.SMSImage;


public interface SMSImageRedisRepository extends CrudRepository<SMSImage, String>{
    
}