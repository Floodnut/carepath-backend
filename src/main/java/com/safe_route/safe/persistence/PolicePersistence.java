package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.PolicePosModel;

@Repository
public interface PolicePersistence extends JpaRepository<PolicePosModel, String>{
    
    @Query(value = "select * from cctvpos p where p.lati > ?1 and p.longti > ?2 and p.lati < ?3 and p.longti < ?4", nativeQuery = true)
    List<PolicePosModel> findPolicePosByLatiGreaterThanAndLongtiGreaterThanAndLatiLessThanAndLongtiLessThan(Double sLati, Double sLongti, Double bLati, Double bLongti);
}
