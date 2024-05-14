package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.node.Tour;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface TourRepository extends Neo4jRepository<Tour, String> {

    @Query("MATCH (t:TOUR{tourId: $tourId})-[:DO]->(p:TOUR_PLACE)" +
            "DETACH DELETE p " +
            "DETACH DELETE t")
    void deleteAllTour(String tourId);
}
