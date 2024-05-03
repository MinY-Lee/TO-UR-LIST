package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {
    //FIXME- Ghost도 나와야함
    @Query("MATCH (t:Tour {tourId: $tourId})-[m:MEMBER]->(u:User) " +
            "RETURN collect(DISTINCT {userId: u.userId, userNickname: u.userNickname, userName: u.userName,memberType:m.memberType})")
    List<Member> findMembersByTourId(String tourId);

    @Query("MATCH (:Tour {tourId: $tourId})-[m:MEMBER]->(:User{userId: $userId}) " +
            "RETURN m.memberType")
    String findMemberTypeByUserIdAndTourId(String userId, String tourId);

    @Query("MATCH (u:User{userId: $userId})-[:ATTEND]->(t:Tour{tourId: $tourId}) create (t)-[:MEMBER {memberType: $memberType}]->(u)")
    void createMemberRelationship(String userId, String tourId, String memberType);

    @Query("MATCH (:User{userId: $hostId})-[r:ATTEND]->(t:Tour{tourId: $tourId}),(u:User{userId: $guestId}) create (t)<-[rel:ATTEND {tourTitle: r.tourTitle}]-(u),(t)-[:MEMBER {memberType: 'guest'}]->(u)")
    void createGuestRelationship(String hostId, String tourId, String guestId);

    @Query("MATCH (u:User{userId: $userId})<-[m:MEMBER]-(t:Tour{tourId: $tourId}) WHERE m.memberType<>'ghost' set m.memberType=$memberType")
    void updateMemberRelationshipExceptGhost(String userId, String tourId, String memberType);

    @Query("MATCH (u:User{userId: $userId})<-[r1:MEMBER {memberType: $memberType}]-(t:Tour{tourId: $tourId}),(u)-[r2:ATTEND]->(t) delete r1,r2")
    void deleteMemberRelationship(String userId, String tourId, String memberType);

    @Query("MATCH (u:User{userId: $userId})-[:ATTEND]->(:Tour{tourId: $tourId}) return u")
    Optional<User> findUserByAttendRelationship(String userId, String tourId);

    @Query("MATCH (t:Tour{tourId: $tourId}) WITH t MATCH (u:User{userId: $userId}) RETURN exists((u)-[:ATTEND]->(t))")
    boolean existsAttendRelationshipByUserIdAndTourId(String userId, String tourId);
}
