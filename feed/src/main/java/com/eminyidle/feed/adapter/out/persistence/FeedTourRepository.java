package com.eminyidle.feed.adapter.out.persistence;

import com.eminyidle.feed.adapter.dto.FeedTourEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedTourRepository extends MongoRepository<FeedTourEntity, String> {
}
