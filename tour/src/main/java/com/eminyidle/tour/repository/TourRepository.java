package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.Tour;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TourRepository extends Neo4jRepository<Tour,String> {

}
