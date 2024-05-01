package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.Do;
import com.eminyidle.place.place.dto.TourPlace;
import com.eminyidle.place.place.dto.node.TourActivity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends Neo4jRepository<TourActivity, String> {

    @Query("MATCH (t:TOUR{tourId: $tourId}) MATCH (a:TOUR_ACTIVITY{tourActivityId: $tourActivityId}) " +
            "CREATE (t)-[d:DO{doId: $doId, placeId: $placeId, placeName: $placeName, tourDay: $tourDay}]->(a)")
    void createDoRelationship(String tourId, String doId, String placeId, String placeName, Integer tourDay, String tourActivityId);


    // 나의 이 여행에 이 날짜에 이 장소가 있는지 보고 없으면 추가해주기

    // 해당 투어의 모든 장소 리스트 조회
    @Query("MATCH (:TOUR{tourId: $tourId}-[r:DO]->(a:TOUR_ACTIVITY)")
    List<TourPlace> findAllByTourId(String tourId);

    // 해당 장소가 추가되었는지 조회
    Optional<TourActivity> findByTourActivityId(String tourActivityId);

//    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO{placeId: $placeId}]->(:TOUR_ACTIVITY)" +
//            "RETURN r")
//    Optional<Do> findPlaceByTourIdAndPlaceId(String tourId, String placeId);
}
