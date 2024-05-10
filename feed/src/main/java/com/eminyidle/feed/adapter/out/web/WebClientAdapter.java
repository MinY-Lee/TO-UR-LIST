package com.eminyidle.feed.adapter.out.web;

import com.eminyidle.feed.application.port.out.LoadTourPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class WebClientAdapter implements LoadTourPort {
    // 실제 구현(요청 넣고 가져오기)

    @Override
    public void loadTour(String tourId) {
        // Tour 반환하는게 맞아용 void -> Tour로 바꿔주기
    }
}
