package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.node.Tour;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;

public interface TourRepository extends Neo4jRepository<Tour, String> {

    @Query("MATCH (t:TOUR{tourId: $tourId})" +
            "OPTIONAL MATCH (t)-[:DO]->(p:TOUR_PLACE)" +
            "WITH t, COLLECT(p) AS places " +
            "FOREACH (place IN places | " +
                "DETACH DELETE place) " +
            "DETACH DELETE t")
    void deleteAllTour(String tourId);

    @Query("MATCH (t:TOUR{tourId: $tourId}) " +
            "WITH t, t.tourPeriod As oldTourPeriod " +
            "SET t.startDate = $start, t.endDate = $end, t.tourPeriod = $period " +
            "WITH t, oldTourPeriod " +
            "OPTIONAL MATCH (t)-[d:DO]->(:TOUR_PLACE) " +
            "WITH t, oldTourPeriod, COLLECT(d) AS do " +
            "FOREACH (d IN do | " +
                "FOREACH (_ IN CASE WHEN oldTourPeriod > $period AND d.tourDay > $period THEN [1] ELSE [] END | " +
                    "SET d.tourDay = 0))")
    void updateTour(String tourId, String tourName, LocalDateTime start, LocalDateTime end, Integer period);
}
