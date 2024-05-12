package com.eminyidle.feed.application.port.out;

import com.eminyidle.feed.adapter.dto.TourInfo;
import reactor.core.publisher.Mono;

public interface LoadTourPort {
    Mono<TourInfo> loadTour(String tourId, String userId);
}
