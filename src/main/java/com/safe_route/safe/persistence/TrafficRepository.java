package com.safe_route.safe.persistence;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.safe_route.safe.model.TrafficModel;

@Repository
public interface TrafficRepository extends JpaRepository<TrafficModel, String>{
    @Query(value = "select traffic.id as id, traffic.congestion as congestion, node.lat as lat, node.lon as lon from nodeim as node, traffic where (traffic.tnode = node.nodeid or traffic.fnode = node.nodeid) and (lat > ?1 and lon > ?2 and lat < ?3 and lon < ?4);", nativeQuery = true)
    List<TrafficModel> findTraffic(Double sLati, Double sLongti, Double bLati, Double bLongti);
}