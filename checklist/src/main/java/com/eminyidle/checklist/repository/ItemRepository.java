package com.eminyidle.checklist.repository;

import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.dto.Item;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends Neo4jRepository<Item,String> {
    //save
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) " +
            "MERGE (i:ITEM {item: $item}) " +
            "MERGE (a)-[r:PRIVATE{userId: $userId}]->(i) "+
            "ON CREATE SET r.createdAt = datetime(), r.type=$itemType,r.isChecked = FALSE " +
//            "ON MATCH SET r.isChecked = FALSE " +
            "RETURN i")
    Optional<Item> save(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, String itemType, LocalDateTime createTime);

    //delete privateRelation

    //add privateRelation

    //add publicRelation
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) MATCH (i:ITEM{item: $item}) CREATE (a)-[:PUBLIC]->(i)")
    void createPublicRelation(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

    @Query("MATCH (t:TOUR{tourId:$tourId})-[p:GO]->()-[:DO]->(a:TOUR_ACTIVITY)-[r:PRIVATE{userId: $userId}]->(i:ITEM) " +
            "RETURN t.tourId AS tourId, p.placeId AS placeId, p.tourDay AS tourDay, a.activity AS activity, i.item AS item, r.isChecked AS isChecked, " +
            "CASE r.type WHEN 'public' THEN TRUE ELSE FALSE END AS isPublic")
    List<ChecklistItemDetail> findChecklistItemDetailByUserIdAndTourId(String userId,String tourId);

    //update privateRelation(checked)
}
