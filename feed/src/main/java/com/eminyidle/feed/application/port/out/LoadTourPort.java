package com.eminyidle.feed.application.port.out;

import com.eminyidle.feed.adapter.dto.TourInfo;

public interface LoadTourPort {
    TourInfo loadTour(String tourId, String userId);
}
