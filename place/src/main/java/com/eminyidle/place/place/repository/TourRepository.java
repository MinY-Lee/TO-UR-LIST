package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.node.Tour;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface TourRepository extends Neo4jRepository<Tour, String> {

    @Query("MATCH (t:TOUR{tourId: $tourId})" +
            "OPTIONAL MATCH (t)-[:DO]->(p:TOUR_PLACE)" +
            "WITH t, COLLECT(p) AS places " +
            "FOREACH (place IN places | " +
                "DETACH DELETE place) " +
            "DETACH DELETE t")
    void deleteAllTour(String tourId);
}
