package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.TourPlaceInfo;
import com.eminyidle.place.place.dto.node.TourPlace;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends Neo4jRepository<TourPlace, String> {

    @Query("MATCH (t:TOUR{tourId: $tourId}) MATCH (a:TOUR_PLACE{tourPlaceId: $tourPlaceId}) " +
            "CREATE (t)-[d:DO{doId: $doId, placeId: $placeId, placeName: $placeName, tourDay: $tourDay}]->(a)")
    void createDoRelationship(String tourId, String doId, String placeId, String placeName, Integer tourDay, String tourPlaceId);


    // 나의 이 여행에 이 날짜에 이 장소가 있는지 보고 없으면 추가해주기

    // 해당 투어의 모든 장소 리스트 조회
    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO]->(a:TOUR_PLACE)")
    List<TourPlaceInfo> findAllByTourId(String tourId);

    // 해당 장소가 추가되었는지 조회
    Optional<TourPlace> findByTourPlaceId(String tourPlaceId);

    // 장소 삭제
    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO{placeId: $placeId, tourDay: $tourDay}]->(a:TOUR_PLACE)" +
            "DETACH DELETE a")
    void deletePlaceByTourIdAndPlaceIdAndTourDay(String tourId, String placeId, Integer tourDay);

    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO{placeId: $placeId, tourDay: $oldTourDay}]->(:TOUR_PLACE)" +
            "SET r.tourDay = $newTourDay")
    void updateTourDay(String tourId, String placeId, Integer oldTourDay, Integer newTourDay);

//    @Query("MATCH (:TOUR{tourId: $tourId})-[r:DO{placeId: $placeId}]->(:TOUR_ACTIVITY)" +
//            "RETURN r")
//    Optional<Do> findPlaceByTourIdAndPlaceId(String tourId, String placeId);
}
