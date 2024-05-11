package com.eminyidle.checklist.repository;

import com.eminyidle.checklist.dto.TourPlace;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TourPlaceRepository extends Neo4jRepository<TourPlace,String> {
    //save
    //delete
}
