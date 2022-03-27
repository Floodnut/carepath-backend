package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.TmapTrafficModel;

@Repository
public interface TmapTrafficRepository extends JpaRepository<TmapTrafficModel, String>{
    @Query(value = "select congestion, lat, lon from tmaptraffic p where p.lat > ?1 and p.lon > ?2 and p.lat < ?3 and p.lon < ?4", nativeQuery = true)
    List<TmapTrafficModel> findTmapTrafficByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(Double sLati, Double sLongti, Double bLati, Double bLongti);
}
