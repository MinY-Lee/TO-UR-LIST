package com.eminyidle.feed.application.port.out;

import com.eminyidle.feed.domain.Feed;

public interface SaveFeedPort {
    void save(Feed feed);
}
