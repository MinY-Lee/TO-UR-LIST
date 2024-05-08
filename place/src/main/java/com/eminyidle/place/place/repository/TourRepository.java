package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.node.Tour;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TourRepository extends Neo4jRepository<Tour, String> {
}
