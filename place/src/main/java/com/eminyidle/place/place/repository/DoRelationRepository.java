package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.Do;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Map;
import java.util.Optional;

public interface DoRelationRepository extends Neo4jRepository<Do, String> {
    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO{placeId: $placeId, tourDay: $tourDay}]->(t:TOUR_PLACE)" +
//            "RETURN id(r) AS id, r.doId AS doId, r.placeName AS placeName, r.placeId AS placeId, r.tourDay AS tourDay")
            "RETURN r.placeId AS placeId")
//            "RETURN r")
//    Optional<Do> findPlaceByTourIdAndPlaceId(String tourId, String placeId);
    Optional<String> findPlaceByTourIdAndPlaceId(String tourId, Integer tourDay, String placeId);
}
