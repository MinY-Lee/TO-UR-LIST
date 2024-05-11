package com.eminyidle.feed.application.port.out;

import reactor.core.publisher.Mono;

public interface LoadTourPort {
    Mono<Void> loadTour(String tourId);
}
