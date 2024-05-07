package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.node.Activity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends Neo4jRepository<Activity, String> {

    List<Activity> findAll();   // 모든 활동 리스트 불러오기

    // 중복된 활동이 있는지 확인
    @Query("MATCH (:TOUR{tourId: $tourId})-[:DO{placeId: $placeId, tourDay: $tourDay}]->(t:TOUR_PLACE)" +
            "-[:REFERENCE]->(a:ACTIVITY{activity: $activity})" +
            "RETURN a")
    Optional<Activity> findActivityByTourIdAndPlaceIdAndActivityId(String tourId, String placeId, Integer tourDay, String activity);

    // 장소에 활동 추가
    @Query("MATCH (:TOUR{tourId: $tourId})-[:DO{placeId: $placeId, tourDay: $tourDay}]->(t:TOUR_PLACE)" +
            "MATCH (a:ACTIVITY{activity: $activity})" +
            "CREATE (t)-[:REFERENCE]->(a)")
    void createReferenceRelationship(String tourId, String placeId, Integer tourDay, String activity);

    // 활동 삭제
    @Query("MATCH (:TOUR_PLACE{tourPlaceId: $tourPlaceId})-[r:REFERENCE]->(:ACTIVITY{activity: $activity})" +
            "DELETE r")
    void deleteByTourPlaceIdAndActivity(String tourPlaceId, String activity);

}
