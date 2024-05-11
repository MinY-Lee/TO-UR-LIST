package com.eminyidle.feed.adapter.out.web;

import com.eminyidle.feed.application.port.out.LoadTourPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebClientAdapter implements LoadTourPort {
    // 실제 구현(요청 넣고 가져오기)
//    WebClient webClient = WebClient.builder().baseUrl()
    WebClient webClient = WebClient.create();

    @Override
    public Mono<Void> loadTour(String tourId) {
        // Tour 반환하는게 맞아용 void -> Tour로 바꿔주기
        // 근데 이제 내가 만들어준 tour인...(짬뽕같은애)
        return null;
    }
}
