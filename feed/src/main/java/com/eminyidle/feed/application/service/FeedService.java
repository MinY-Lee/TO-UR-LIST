package com.eminyidle.feed.application.service;

import com.eminyidle.feed.adapter.dto.HiddenActivity;
import com.eminyidle.feed.adapter.dto.HiddenItem;
import com.eminyidle.feed.adapter.dto.HiddenPlace;
import com.eminyidle.feed.adapter.dto.TourInfo;
import com.eminyidle.feed.application.port.in.CreateFeedUsecase;
import com.eminyidle.feed.application.port.out.LoadTourPort;
import com.eminyidle.feed.application.port.out.SaveFeedPort;
import com.eminyidle.feed.domain.Feed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
        saveFeedPort.save(feed);

    }

}
