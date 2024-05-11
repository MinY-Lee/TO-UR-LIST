package com.eminyidle.feed.adapter.out.web;

import com.eminyidle.feed.adapter.dto.Place;
import com.eminyidle.feed.adapter.dto.PlaceList;
import com.eminyidle.feed.adapter.dto.TourInfo;
import com.eminyidle.feed.application.port.out.LoadTourPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebClientAdapter implements LoadTourPort {
    // 실제 구현(요청 넣고 가져오기)
//    WebClient webClient = WebClient.builder().baseUrl()
    WebClient webClient = WebClient.create();

    @Value("${PLACE_SERVER_URL}")
    private String PLACE_SERVER_URL;
    @Value("${TOUR_SERVER_URL}")
    private String TOUR_SERVER_URL;
    @Value("${CHECKLIST_SERVER_URL}")
    private String CHECKLIST_SERVER_UR;

    @Override
    public Mono<TourInfo> loadTour(String tourId) {
        // Tour 반환하는게 맞아용 void -> Tour로 바꿔주기
        // 근데 이제 내가 만들어준 tour인...(짬뽕같은애)
        log.info("loadTour 실행");
        Mono<List<Place>> placeRes = webClient.get().uri(PLACE_SERVER_URL + "/place/" + tourId).retrieve().bodyToMono(new ParameterizedTypeReference<List<Place>>(){});
        Mono<List<Place>> placeRes2 = webClient.get().uri(PLACE_SERVER_URL + "/place/" + tourId).retrieve().bodyToMono(new ParameterizedTypeReference<List<Place>>(){});


//        Flux<PlaceList> placeRes = webClient.get().uri(PLACE_SERVER_URL + "/place/" + tourId).retrieve().bodyToMono(PlaceList.class);

        log.info("web client 실행");
        Mono.zip(placeRes, placeRes2).doOnSuccess(tuple -> {
            log.info(tuple.toString());
            String myplace = tuple.getT2().get(1).getTourDay().toString();
            System.out.println(myplace);
        }).block();
        log.info("zip 실행 완료");

        return null;
    }
}
