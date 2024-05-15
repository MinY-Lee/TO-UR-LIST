package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.application.dto.TourPlace;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface TourPlaceRepository extends Neo4jRepository<TourPlace,String> {

    @Query("OPTIONAL MATCH (:TOUR{tourId: $tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(p:TOUR_PLACE) RETURN CASE WHEN p IS NULL THEN FALSE ELSE TRUE END")
    boolean existsByTourIdAndPlaceIdAndTourDay(String tourId, String placeId, Integer tourDay);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO]->(p:TOUR_PLACE) RETURN p")
    List<TourPlace> findAllByTourId(String tourId);
    //delete
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(p:TOUR_PLACE) DETACH DELETE p")
    void delete(String tourId, String placeId, Integer tourDay);
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(p:TOUR_PLACE) RETURN p")
    Optional<TourPlace> findByTourIdAndPlaceIdAndTourDay(String tourId, String placeId, Integer tourDay);

    @Query("MATCH (:TOUR)-[g:GO]->(:TOUR_PLACE{tourPlaceId: $tourPlaceId}) set g.tourDay=$tourDay")
    void updateTourDayOfTourPlace(String tourPlaceId, Integer tourDay);

}
