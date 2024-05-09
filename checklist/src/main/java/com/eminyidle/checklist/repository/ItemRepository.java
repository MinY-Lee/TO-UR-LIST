package com.eminyidle.checklist.repository;

import com.eminyidle.checklist.dto.Item;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ItemRepository extends Neo4jRepository<Item,String> {
    //save

    //delete privateRelation

    //add privateRelation

    //add publicRelation

    //update privateRelation(checked)
}
