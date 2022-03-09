package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.SafePosModel;

@Repository
public interface SafePersistence extends JpaRepository<SafePosModel, String>{
    
    @Query(value = "select * from safepos p where p.lati > ?1 and p.longi > ?2 and p.lati < ?3 and p.longi < ?4", nativeQuery = true)
    List<SafePosModel> findSafePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(Double sLati, Double sLongti, Double bLati, Double bLongti);
}