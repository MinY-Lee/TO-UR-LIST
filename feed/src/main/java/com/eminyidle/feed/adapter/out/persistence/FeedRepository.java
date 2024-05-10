package com.eminyidle.feed.adapter.out.persistence;

import com.eminyidle.feed.adapter.dto.FeedEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FeedRepository extends Neo4jRepository<FeedEntity, String> {

}
