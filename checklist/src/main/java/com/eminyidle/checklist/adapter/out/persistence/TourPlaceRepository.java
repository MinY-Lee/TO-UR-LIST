package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.application.dto.Go;
import com.eminyidle.checklist.application.dto.TourActivity;
import com.eminyidle.checklist.application.dto.TourPlace;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface TourPlaceRepository extends Neo4jRepository<TourPlace,String> {
    //save
//    @Query("MATCH (t:TOUR{tourId: $tourId}) MERGE")
//    Optional<TourPlace> save(String tourPlaceId, String placeId, Integer tourDay,String tourId);
    @Query("MATCH (t:TOUR_PLACE{tourPlaceId: $tourPlaceId}) RETURN t")
    Optional<TourPlace> findByTourPlaceId(String tourPlaceId);

    @Query("OPTIONAL MATCH (t:TOUR_PLACE{tourPlaceId: $tourPlaceId}) RETURN CASE WHEN t IS NULL THEN FALSE ELSE TRUE END")
    boolean existsByTourPlaceId(String tourPlaceId);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO]->(p:TOUR_PLACE) RETURN p")
    List<TourPlace> findAllByTourId(String tourId);
    //delete
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(p:TOUR_PLACE) DETACH DELETE p")
    void delete(String tourId, String placeId, Integer tourDay);
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(p:TOUR_PLACE) RETURN p")
    Optional<TourPlace> findByTourIdAndPlaceIdAndTourDay(String tourId, String placeId, Integer tourDay);
}
