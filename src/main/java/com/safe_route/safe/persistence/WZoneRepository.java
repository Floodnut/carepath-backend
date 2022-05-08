package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.WZoneModel;

@Repository
public interface WZoneModelRepository extends JpaRepository<WZoneModel, String>{
    @Query(value = "select * from wzone where (lati > ?1 and longi > ?2 and lati < ?3 and longi < ?4);", nativeQuery = true)
    List<WZoneModel> findWZone(Double sLati, Double sLongti, Double bLati, Double bLongti);
}