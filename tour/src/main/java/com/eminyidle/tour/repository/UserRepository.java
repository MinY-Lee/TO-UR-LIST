package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH (t:Tour {tourId: $tourId})-[m:MEMBER]->(u:User) " +
            "RETURN collect(DISTINCT {userId: u.userId, userNickname: u.userNickname, userName: u.userName,memberType:m.memberType})")
    List<Member> findMembersByTourId(String tourId);

    @Query("MATCH (:Tour {tourId: $tourId})-[m:MEMBER]->(:User{userId: $userId}) " +
            "RETURN m.memberType")
    String findMemberTypeByUserIdAndTourId(String userId, String tourId);
}
