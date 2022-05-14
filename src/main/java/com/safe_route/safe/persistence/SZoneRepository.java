package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.SZoneModel;

@Repository
public interface SZoneRepository extends JpaRepository<SZoneModel, String>{
    @Query(value = "select * from szone where (lati > ?1 and longi > ?2 and lati < ?3 and longi < ?4);", nativeQuery = true)
    List<SZoneModel> findAllSZone(Double sLati, Double sLongi, Double bLati, Double bLongi);
}