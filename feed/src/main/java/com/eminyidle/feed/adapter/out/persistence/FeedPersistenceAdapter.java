package com.eminyidle.feed.adapter.out.persistence;

import com.eminyidle.feed.adapter.dto.FeedEntity;
import com.eminyidle.feed.application.port.out.LoadTourPort;
import com.eminyidle.feed.application.port.out.SaveFeedPort;
import com.eminyidle.feed.domain.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional("transactionManager")
public class FeedPersistenceAdapter  implements SaveFeedPort {

    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper;

    @Override
    public void save(Feed feed) {
        FeedEntity feedEntity = feedMapper.toEntity(feed);
        feedRepository.save(feedEntity);

    }
    // loadTour
}
