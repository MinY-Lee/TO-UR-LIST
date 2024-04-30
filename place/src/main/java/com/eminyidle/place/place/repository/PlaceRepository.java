package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.TourActivity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<TourActivity, String> {

    @Query("MATCH (t:Tour{tourId: $tourId}) MATCH (a:TourActivity{tourActivityId: $tourActivityId}) " +
            "CREATE (t)-[d:DO{placeId: $placeId, placeName: $placeName, tourDay: $tourDay}]->(a)")
    void createDoRelationship(String tourId, String placeId, String placeName, Integer tourDay, String tourActivityId);


    // 나의 이 여행에 이 날짜에 이 장소가 있는지 보고 없으면 추가해주기
}
