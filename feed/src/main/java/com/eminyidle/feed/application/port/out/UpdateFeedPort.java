package com.eminyidle.feed.application.port.out;

import com.eminyidle.feed.adapter.dto.FeedTourEntity;
import com.eminyidle.feed.domain.Feed;

public interface UpdateFeedPort {
    void update(Feed feed);

    void update(FeedTourEntity feedTourEntity);
}
