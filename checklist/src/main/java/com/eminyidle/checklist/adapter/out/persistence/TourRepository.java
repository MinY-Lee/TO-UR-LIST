package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.application.dto.Tour;
import com.eminyidle.checklist.application.dto.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends Neo4jRepository<Tour,String> {

    @Query("MATCH (t:TOUR {tourId: $tourId}) RETURN t")
    Optional<Tour> findByTourId(String tourId);

//    @Query("MATCH (t:TOUR{tourId: $tourId})-[:MEMBER]->(:USER{userId:$userId}) RETURN t,collect{MATCH (t)-[:MEMBER]->(u:USER) RETURN u} AS memberList")
    @Query("MATCH (t:TOUR {tourId: $tourId})-[:MEMBER]->(u:USER {userId: $userId}) " +
            "WITH t MATCH p=(t)-[:MEMBER]->(m:USER) " +
            "RETURN collect(p)")
    Optional<Tour> findTourByUserIdAndTourId(String userId, String tourId);

//    @Query("MATCH (:TOUR {tourId: $tourId})-[:MEMBER]->(m:USER) RETURN m")
//  위의 쿼리는 문제 생김?!? 에러메시지는..
//Cannot retrieve a value for property `userId` of DTO `com.eminyidle.checklist.dto.User` and the property will always be null. Make sure to project only properties of the domain type or use a custom query that returns a mappable data under the name `userId`.
    @Query("MATCH (:TOUR {tourId: $tourId})-[:MEMBER]->(m:USER) RETURN m.userId AS userId")
    List<User> findMemberByTourId(String tourId);

    @Query("MATCH (t:TOUR {tourId: $tourId}) WITH t MATCH (c:COUNTRY{countryCode: $countryCode}) MERGE (t)-[:TO]->(c) ")
    void createToRelationshipBetweenTourAndCountry(String tourId, String countryCode);

    @Query("MATCH (:TOUR {tourId: $tourId})-[to:TO]->(:COUNTRY{countryCode: $countryCode}) DELETE to")
    void deleteToRelationshipBetweenTourAndCountry(String tourId, String countryCode);

    @Query("OPTIONAL MATCH (:TOUR{tourId: $tourId})-[r:MEMBER]->(:USER{userId: $userId}) RETURN CASE WHEN r IS NULL THEN FALSE ELSE TRUE END")
    boolean existsMemberRelationshipByTourIdAndUserId(String tourId,String userId);
    @Query("MATCH (t:TOUR{tourId: $tourId}) MERGE (u:USER{userId: $userId}) CREATE (t)-[:MEMBER]->(u)")
    void createMemberRelationshipByTourIdAndUserId(String tourId,String userId);
}
