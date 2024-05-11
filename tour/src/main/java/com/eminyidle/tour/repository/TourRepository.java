package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.Tour;
import com.eminyidle.tour.dto.TourDetail;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends Neo4jRepository<Tour, String> {

    @Query("MATCH (:USER{userId: $userId})-[at:ATTEND]->(t:TOUR {tourId: $tourId}) " +
            "RETURN t.tourId AS tourId, t.startDate AS startDate, t.endDate AS endDate, at.tourTitle AS tourTitle")
    Optional<TourDetail> findTourDetailByUserIdAndTourId(String userId, String tourId);

    @Query("MATCH (:USER{userId: $userId})<-[:MEMBER{memberType:'host'}]-(t:TOUR{tourId: $tourId}) return t")
    Optional<Tour> findTourByHostUserIdAndTourId(String userId, String tourId);
    @Query("MATCH (:USER{userId: $userId})-[r:ATTEND]->(t:TOUR) " +
            "MATCH p=(t)-[:TO]->(:CITY) " +
            "RETURN r.tourTitle AS tourTitle, p")
    List<Tour> findAllToursByUserId(String userId);

    //현재 관계가 없으면(MATCH가 없으면) 그냥 아무 일도 벌어지지 않는다
    @Query("MATCH (:USER{userId: $userId})-[r:ATTEND]->(:TOUR{tourId: $tourId}) set r.tourTitle=$tourTitle")
    void updateTourTitle(String userId, String tourId, String tourTitle);

    @Query("MATCH (:USER{userId: $userId})-[:ATTEND]->(t:TOUR{tourId: $tourId}) RETURN t")
//    @Query("MATCH (:USER{userId: $userId})-[r:ATTEND]->(t:TOUR{tourId: $tourId}) " +
//            "MATCH p=(t)-[:TO]->(:CITY) " +
//            "RETURN r.tourTitle AS tourTitle, p")
    Optional<Tour> findByUserIdAndTourId(String userId, String tourId);

    @Query("MATCH (:TOUR{tourId: $tourId})-[r]->(:CITY{cityName:$cityName}) delete r")
    void deleteTourCityRelationshipByTourIdAndCityName(String tourId, String cityName);

}

