package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.dto.Take;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface TakeRepository extends Neo4jRepository<Take, Long> {

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{userId: $userId}]->(i:ITEM {item: $item}) RETURN r.createdAt AS createdAt, r.type AS type, r.isChecked AS isChecked, r.userId AS userId, i AS item ")
    Optional<Take> findTakeRelationshipByUserId(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{type:'public'}]->(i:ITEM {item: $item}) RETURN r.createdAt AS createdAt, r.type AS type, r.isChecked AS isChecked, r.userId AS userId, i AS item ")
    List<Take> findTakePublicRelationshipList(String tourId, String placeId, Integer tourDay, String activity, String item);

    @Query("OPTIONAL MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) WITH a OPTIONAL MATCH (i:ITEM {item: $item}) RETURN EXISTS((a)-[:TAKE{userId: $userId}]->(i)) ")
    boolean existsTakeRelationshipByUserId(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);
    @Query("OPTIONAL MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) WITH a OPTIONAL MATCH (i:ITEM {item: $item}) RETURN EXISTS((a)-[:PUBLIC]->(i)) ")
    boolean existsPublicRelationship(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

}
