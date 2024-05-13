package com.eminyidle.tour.adapter.out.persistence.neo4j;

import com.eminyidle.tour.application.dto.Ghost;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface GhostRepository extends Neo4jRepository<Ghost,String> {
    @Query("MATCH (g:GHOST{ghostNickname: $ghostNickname})<-[:MEMBER]-(:TOUR{tourId: $tourId}) RETURN g")
    Ghost findByGhostNicknameAndTourId(String ghostNickname,String tourId);

    @Query("MATCH (g:GHOST{ghostId: $ghostId}) WITH g MATCH (t:TOUR{tourId: $tourId}) create (t)-[:MEMBER {memberType: 'ghost'}]->(g)")
    void createGhostRelationship(String ghostId, String tourId);

    @Query("MATCH (t:TOUR{tourId: $tourId}) WITH t OPTIONAL MATCH (g:GHOST {ghostNickname: $ghostNickname}) RETURN exists((t)-[:MEMBER]->(g))")
    boolean existsGhostByGhostNicknameAndTourId(String ghostNickname,String tourId);
}
