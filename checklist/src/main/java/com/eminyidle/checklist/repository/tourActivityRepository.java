package com.eminyidle.checklist.repository;

import com.eminyidle.checklist.dto.TourActivity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface tourActivityRepository extends Neo4jRepository<TourActivity,String> {
    //save: activity의 NEED관계 다 public으로 연결해두기....
    //delete
}
