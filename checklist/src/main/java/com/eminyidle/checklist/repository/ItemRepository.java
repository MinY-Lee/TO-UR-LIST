package com.eminyidle.checklist.repository;

import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.dto.Item;
import com.eminyidle.checklist.dto.Take;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends Neo4jRepository<Item,String> {
    //save
//    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) " +
//            "MERGE (i:ITEM {item: $item}) " +
//            "MERGE (a)-[r:TAKE{userId: $userId}]->(i) "+
//            "ON CREATE SET r.createdAt = localdatetime(), r.type=$itemType,r.isChecked = FALSE " +
//            "ON MATCH SET r.type=$itemType " +
//            "RETURN i")
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) " +
            "MERGE (i:ITEM {item: $item}) " +
            "MERGE (a)-[r:TAKE{userId: $userId}]->(i) "+
            "ON CREATE SET r.createdAt = $createdAt, r.type=$itemType,r.isChecked = $isChecked " +
            "ON MATCH SET r.type=$itemType " +
            "RETURN i")
    Optional<Item> save(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, String itemType, LocalDateTime createdAt, boolean isChecked);

    // find
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity})-[:TAKE{userId: $userId}]->(i:ITEM {item: $item}) "+
            "RETURN i")
    Optional<Item> findItem(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

    // 이 아래거는 안먹힘.........PrivateItem이 Relationship으로 작용한다는 것 자체를 인식 못하는듯?
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{userId: $userId}]->(i:ITEM {item: $item}) " +
            "RETURN r.createdAt AS createdAt, r.type AS type, r.isChecked AS isChecked, r.userId AS userId, i AS item ")
    Take findPrivateRelationship(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);


    //delete privateRelation
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{userId: $userId}]->(:ITEM {item: $item}) DELETE r")
    void deletePrivateItemRelationship(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:PUBLIC]->(:ITEM {item: $item}) DELETE r ")
    void deletePublicRelationship(String tourId, String placeId, Integer tourDay, String activity, String item);

    //add privateRelation

    //add publicRelation
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) MERGE (i:ITEM{item: $item}) MERGE (a)-[:PUBLIC]->(i)")
    void createPublicRelation(String userId, String tourId, String placeId, Integer tourDay, String activity, String item);

    @Query("MATCH (t:TOUR{tourId:$tourId})-[p:GO]->()-[:DO]->(a:TOUR_ACTIVITY)-[r:TAKE{userId: $userId}]->(i:ITEM) " +
            "RETURN t.tourId AS tourId, p.placeId AS placeId, p.tourDay AS tourDay, a.activity AS activity, i.item AS item, r.isChecked AS isChecked, " +
            "CASE r.type WHEN 'public' THEN TRUE ELSE FALSE END AS isPublic" +
            "ORDER BY r.createdAt")
    List<ChecklistItemDetail> findChecklistItemDetailByUserIdAndTourId(String userId,String tourId);

    //update privateRelation(checked)
    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{userId: $userId}]->(:ITEM {item: $item}) " +
            "SET r.isChecked = $isChecked")
    void updateItemIsChecked(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, boolean isChecked);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(:TOUR_ACTIVITY{activity: $activity})-[r:TAKE{userId: $userId}]->(:ITEM {item: $item}) " +
            "SET r.isChecked = $isChecked, r.createdAt = $createdAt")
    void updateItemAsIsByIsCheckedAndCreatedAt(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, Boolean isChecked, LocalDateTime createdAt);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) " +
            "MERGE (i:ITEM {item: $item}) " +
            "MERGE (a)-[r:TAKE{userId: $userId}]->(i) "+
            "ON CREATE SET r.createdAt = $createdAt, r.type='private', r.isChecked = $isChecked " +
//            "ON MATCH SET r.isChecked = FALSE " +
            "RETURN i")
    Optional<Item> update(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, boolean isChecked, LocalDateTime createdAt);

    @Query("MATCH (:TOUR{tourId:$tourId})-[:GO{placeId: $placeId, tourDay: $tourDay}]->()-[:DO]->(a:TOUR_ACTIVITY{activity: $activity}) " +
            "MERGE (i:ITEM {item: $item}) " +
            "MERGE (a)-[r:TAKE{userId: $userId}]->(i) "+
            "ON CREATE SET r.createdAt = localdatetime(), r.type=$itemType,r.isChecked = FALSE " +
//            "ON MATCH SET r.isChecked = FALSE " +
            "RETURN i")
    boolean existsT(String userId, String tourId, String placeId, Integer tourDay, String activity, String item, String itemType, LocalDateTime createTime);



}
