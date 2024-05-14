package com.eminyidle.feed.application.service;

import com.eminyidle.feed.adapter.dto.*;
import com.eminyidle.feed.application.port.in.CreateFeedUsecase;
import com.eminyidle.feed.application.port.in.UpdateFeedUsecase;
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
public class FeedService implements CreateFeedUsecase, UpdateFeedUsecase {

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

        // day, place 필터링
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

        // activity 필터링
        hiddenActivityList.forEach(hiddenActivity -> {
            log.info("start filter hiddenActivity");
            filteredPlace.forEach(place -> {
                if (hiddenActivity.getPlaceId().equals(place.getPlaceId()) && hiddenActivity.getTourDay().equals(place.getTourDay())) {
                    place.getActivityList().remove(hiddenActivity.getActivity());
                }
            });
        });

        // TODO ITEM 걸러주기 확인
        // item 필터링
        List<Item> filteredItem = tourInfo.getItemList().stream().filter(item -> {
            for (HiddenItem hiddenItem : hiddenItemList) {
                if (item.getActivity().equals(hiddenItem.getActivity()) && item.getPlaceId().equals(hiddenItem.getPlaceId()) && item.getTourDay() == hiddenItem.getTourDay())
                    return false;
            }
            return true;
        }).collect(Collectors.toList());

        log.info(filteredPlace.toString());
        // mongoDB 저장을 위한
        FeedTourEntity feedTourEntity = FeedTourEntity.builder()
                .feedId(feedId)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .userNickname(tourInfo.getUserNickname())
                .feedThemeTagList(feedThemeTagList)
                .feedMateTag(feedMateTag)
                .startDate(tourInfo.getStartDate())
                .cityList(tourInfo.getCityList())
                .placeList(filteredPlace)
                .itemList(filteredItem)
                .copyCount(0)
                .likeCount(0)
                .isLiked(false)
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
        saveFeedPort.save(feedTourEntity);
        saveFeedPort.save(feed);

    }

    @Override
    public void updateFeed(String feedId, String userId, String feedTitle, String feedContent, List<String> feedThemeTagList, String feedMateTag,
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
    }
}
