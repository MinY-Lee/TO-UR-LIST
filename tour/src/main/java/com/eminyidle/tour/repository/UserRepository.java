package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {
    //FIXME- Ghost도 나와야함
    @Query("MATCH (t:TOUR {tourId: $tourId})-[m:MEMBER]->(u:USER) " +
            "RETURN collect(DISTINCT {userId: u.userId, userNickname: u.userNickname, userName: u.userName,memberType:m.memberType})")
    List<Member> findMembersByTourId(String tourId);

    @Query("MATCH (:TOUR {tourId: $tourId})-[m:MEMBER]->(:USER{userId: $userId}) " +
            "RETURN m.memberType")
    String findMemberTypeByUserIdAndTourId(String userId, String tourId);

    @Query("MATCH (u:USER{userId: $userId})-[:ATTEND]->(t:TOUR{tourId: $tourId}) create (t)-[:MEMBER {memberType: $memberType}]->(u)")
    void createMemberRelationship(String userId, String tourId, String memberType);

    @Query("MATCH (:USER{userId: $hostId})-[r:ATTEND]->(t:TOUR{tourId: $tourId})" +
            "WITH r,t OPTIONAL MATCH(u:USER{userId: $guestId}) " +
            "CREATE (t)<-[rel:ATTEND {tourTitle: r.tourTitle}]-(u),(t)-[:MEMBER {memberType: 'guest'}]->(u)")
    void createGuestRelationship(String hostId, String tourId, String guestId);

    @Query("MATCH (u:USER{userId: $userId})<-[m:MEMBER]-(t:TOUR{tourId: $tourId}) WHERE m.memberType<>'ghost' set m.memberType=$memberType")
    void updateMemberRelationshipExceptGhost(String userId, String tourId, String memberType);

    @Query("MATCH (u:USER{userId: $userId})<-[r1:MEMBER {memberType: 'guest'}]-(t:TOUR{tourId: $tourId}) WITH u,r1,t MATCH (u)-[r2:ATTEND]->(t) delete r1,r2")
    void deleteGuestRelationship(String userId, String tourId);

    @Query("MATCH (u:USER{userId: $userId})-[:ATTEND]->(:TOUR{tourId: $tourId}) return u")
    Optional<User> findUserByAttendRelationship(String userId, String tourId);

    @Query("MATCH (t:TOUR{tourId: $tourId}) WITH t MATCH (u:USER{userId: $userId}) RETURN exists((u)-[:ATTEND]->(t))")
    boolean existsAttendRelationshipByUserIdAndTourId(String userId, String tourId);
}
