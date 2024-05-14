package com.eminyidle.feed.adapter.out.web;

import com.eminyidle.feed.adapter.dto.Place;
import com.eminyidle.feed.adapter.dto.Tour;
import com.eminyidle.feed.adapter.dto.TourInfo;
import com.eminyidle.feed.adapter.dto.User;
import com.eminyidle.feed.application.port.out.LoadTourPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebClientAdapter implements LoadTourPort {
    // 실제 구현(요청 넣고 가져오기)
//    WebClient webClient = WebClient.builder().baseUrl()
//    WebClient webClient = WebClient.create();
    WebClient webClient = WebClient.create();

    @Value("${USER_SERVER_URL}")
    private String USER_SERVER_URL;
    @Value("${PLACE_SERVER_URL}")
    private String PLACE_SERVER_URL;
    @Value("${TOUR_SERVER_URL}")
    private String TOUR_SERVER_URL;
    @Value("${CHECKLIST_SERVER_URL}")
    private String CHECKLIST_SERVER_UR;

    private TourInfo tourInfo = null;

    @Override
    public TourInfo loadTour(String tourId, String userId) {
        // Tour 반환하는게 맞아용 void -> Tour로 바꿔주기
        // 근데 이제 내가 만들어준 tour인...(짬뽕같은애)

        // request Header 설정을 위한 build
        webClient = WebClient.builder()
                .defaultHeader("UserId", userId)
                .build();
        log.info("loadTour 실행");
        log.info(tourId);
        Mono<List<Place>> placeRes = webClient.get().uri(PLACE_SERVER_URL + "/place/" + tourId).retrieve().bodyToMono(new ParameterizedTypeReference<List<Place>>(){});
        Mono<Tour> tourRes = webClient.get().uri(TOUR_SERVER_URL + "/tour/" + tourId).retrieve().bodyToMono(new ParameterizedTypeReference<Tour>(){});
        Mono<User> userRes = webClient.get().uri(USER_SERVER_URL + "/user").retrieve().bodyToMono(new ParameterizedTypeReference<User>(){});

        // TODO item 호출 구현


        log.info("web client 실행");

        Mono.zip(placeRes, tourRes, userRes).doOnSuccess(tuple -> {
            log.info(tuple.toString());
            tourInfo = TourInfo.builder()
                    .userNickname(tuple.getT3().getUserNickname())
                    .startDate(tuple.getT2().getStartDate())
                    .endDate(tuple.getT2().getEndDate())
                    .cityList(tuple.getT2().getCityList())
                    .placeList(tuple.getT1())
                    .build();
            log.info(tourInfo.toString());
        }).block();
        log.info("zip 실행 완료");

        return tourInfo;
    }
}
