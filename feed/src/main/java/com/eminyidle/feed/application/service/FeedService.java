package com.eminyidle.feed.application.service;

import com.eminyidle.feed.adapter.dto.*;
import com.eminyidle.feed.application.port.in.CreateFeedUsecase;
import com.eminyidle.feed.application.port.out.LoadTourPort;
import com.eminyidle.feed.application.port.out.SaveFeedPort;
import com.eminyidle.feed.domain.Feed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService implements CreateFeedUsecase {

    private final SaveFeedPort saveFeedPort;
    private final LoadTourPort loadTourPort;

    @Override
    public void createFeed(String feedId, String userId, String feedTitle, String feedContent, List<String> feedThemeTagList, String feedMateTag,
                           String tourId, List<Integer> hiddenDayList, List<HiddenPlace> hiddenPlaceList,
                           List<HiddenActivity> hiddenActivityList, List<HiddenItem> hiddenItemList) {

        Feed feed = Feed.builder()
                .feedId(feedId)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .feedThemeTagList(feedThemeTagList)
                .feedMateTag(feedMateTag)
                .tourId(tourId)
                .hiddenDayList(hiddenDayList)
                .hiddenPlaceList(hiddenPlaceList)
                .hiddenActivityList(hiddenActivityList)
                .hiddenItemList(hiddenItemList)
                .build();

        // 외부 api 호출을 신청한다?
        // 에러처리는 서비스에서 해주기
        TourInfo tourInfo = loadTourPort.loadTour(tourId, userId);
        log.info("loadTour 실행 완료");
        log.info(tourInfo.toString());

        List<Place> filteredPlace = tourInfo.getPlaceList().stream().filter(place -> {
                    // 숨기고싶은 날짜 지우기
                    if (hiddenDayList.contains(place.getTourDay())) return false;
                    // 숨기고싶은 장소 지우기
                    for (HiddenPlace hiddenPlace : hiddenPlaceList) {
                        log.info("for문 실행중");
                        if (place.getPlaceId().equals(hiddenPlace.getPlaceId()) && place.getTourDay().equals(hiddenPlace.getTourDay()))
                            return false;
                    }
                    return true;
                }).collect(Collectors.toList());

        // Activity 거르기
        log.info(filteredPlace.toString());
        // mongoDB 저장을 위한
        FeedTourEntity feedTourEntity = FeedTourEntity.builder()
                .feedId(feedId)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .userNickname(tourInfo.getUserNickname())
                .feedThemeTagList(feedThemeTagList)
                .cityList(tourInfo.getCityList())
//                .placeList(tourInfo.getPlaceList() == null ? new ArrayList<>() : tourInfo.getPlaceList().stream().filter(place -> {
//                            // 숨기고싶은 날짜 지우기
//                            if (hiddenDayList.contains(place.getTourDay())) return false;
//                            // 숨기고싶은 장소 지우기
//                            for (HiddenPlace hiddenPlace : hiddenPlaceList) {
//                                log.info("for문 실행중");
//                                if (place.getPlaceId().equals(hiddenPlace.getPlaceId()) && place.getTourDay().equals(hiddenPlace.getTourDay()))
//                                    return false;
//                            }
                            // 숨기고싶은 활동 지우기
//                            place.getActivityList().stream().
//                            place.setActivityList(place.getActivityList() == null ? new ArrayList<>() : place.getActivityList().stream().filter(activity -> {
//                                if (activity.equals(hidd))
//                            }));
//
//                                return true;
//                        })
//                        (!hiddenDayList.contains(place.getTourDay()))||
//                                (!hiddenPlaceList.contains(place.getPlaceId())))
//                        &&hiddenPlaceList.contains(place.getTourDay())
//                .collect(Collectors.toList())
                .build();
        log.info(feedTourEntity.toString());
        saveFeedPort.save(feed);

    }

}
