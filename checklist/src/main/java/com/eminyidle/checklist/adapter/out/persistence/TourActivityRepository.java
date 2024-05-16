package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.application.dto.TourActivity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface TourActivityRepository extends Neo4jRepository<TourActivity,String> {
    //save: activity의 NEED관계 다 public으로 연결해두기....
    @Query("MATCH (p:TOUR_PLACE{tourPlaceId: $tourPlaceId}) WITH p MATCH (a:ACTIVITY{activity: $activity}) " +
            "CREATE (p)-[:DO]->(x:TOUR_ACTIVITY{activity:$activity, tourActivityId: $tourActivityId})-[:REFERENCE]->(a) RETURN x")
    TourActivity save(String tourPlaceId, String activity, String tourActivityId);
    //delete
    @Query("MATCH (:TOUR_PLACE{tourPlaceId: $tourPlaceId})-[:DO]->(x:TOUR_ACTIVITY{activity:$activity}) DETACH DELETE x")
    void delete(String tourPlaceId, String activity);

    //findAll
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->(:TOUR_PLACE)-[:DO]->(a:TOUR_ACTIVITY) RETURN a")
    List<TourActivity> findAllByTourPlace(String tourId, String placeId, Integer tourDay);
    @Query("MATCH (:TOUR_PLACE{tourPlaceId: $tourPlaceId})-[:DO]->(a:TOUR_ACTIVITY) RETURN a")
    List<TourActivity> findAllByTourPlaceId(String tourPlaceId);

    //merge
    @Query("OPTIONAL MATCH (:TOUR_PLACE{tourPlaceId: $tourPlaceId})-[:DO]->(a:TOUR_ACTIVITY)-[:REFERENCE]->(:ACTIVITY{activity: $activityName}) RETURN CASE WHEN a IS NULL THEN FALSE ELSE TRUE END")
    boolean existsByTourPlaceIdAndActivity(String tourPlaceId, String activityName);
    @Query("MATCH (tp:TOUR_PLACE)-[r:DO]->(a:TOUR_ACTIVITY{tourActivityId:$tourActivityId}) DELETE r WITH a MATCH (p:TOUR_PLACE{tourPlaceId:$targetTourPlaceId}) CREATE (p)-[:DO]->(a)")
    void updateTourPlaceByTourActivtyIdAndTargetTourPlaceId(String tourActivityId, String targetTourPlaceId);

}
