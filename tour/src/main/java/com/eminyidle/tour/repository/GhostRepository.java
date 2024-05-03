package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.Ghost;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface GhostRepository extends Neo4jRepository<Ghost,String> {
    @Query("MATCH (g:GHOST{ghostNickname: $ghostNickname})<-[:MEMBER]-(:Tour{tourId: $tourId}) RETURN g")
    Ghost findByGhostNicknameAndTourId(String ghostNickname,String tourId);

    @Query("MATCH (g:GHOST{ghostId: $ghostId}),(t:Tour{tourId: $tourId}) create (t)-[:MEMBER {memberType: 'ghost'}]->(g)")
    void createGhostRelationship(String ghostId, String tourId);
}
