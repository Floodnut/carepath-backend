package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.AccidentModel;

@Repository
public interface AccidentRepository extends JpaRepository<AccidentModel, String>{
    @Query(value = "select * from accident p where p.lati > ?1 and p.longi > ?2 and p.lati < ?3 and p.longi < ?4", nativeQuery = true)
    List<AccidentModel> findAccidentByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(Double sLati, Double sLongi, Double bLati, Double bLongi);
}
