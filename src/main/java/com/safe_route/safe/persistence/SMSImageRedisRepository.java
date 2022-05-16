package com.safe_route.safe.persistence;

import org.springframework.data.repository.CrudRepository;
import com.safe_route.safe.model.SMSImage;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SMSImageRedisRepository extends CrudRepository<SMSImage, String>{

}