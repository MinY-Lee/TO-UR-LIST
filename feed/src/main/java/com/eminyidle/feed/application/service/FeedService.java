package com.eminyidle.feed.application.service;

import com.eminyidle.feed.adapter.dto.HiddenActivity;
import com.eminyidle.feed.adapter.dto.HiddenItem;
import com.eminyidle.feed.adapter.dto.HiddenPlace;
import com.eminyidle.feed.application.port.in.CreateFeedUsecase;
import com.eminyidle.feed.application.port.out.SaveFeedPort;
import com.eminyidle.feed.domain.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService implements CreateFeedUsecase {

    private final SaveFeedPort saveFeedPort;

    @Override
    public void createFeed(String feedId, String feedTitle, String feedContent, List<String> feedThemeTagList, String feedMateTag,
                           String tourId, List<Integer> hiddenDayList, List<HiddenPlace> hiddenPlaceList,
                           List<HiddenActivity> hiddenActivityList, List<HiddenItem> hiddenItemList) {

        Feed feed = Feed.builder()
                .feedId(feedId)
                .feedTitle(feedTitle)
                .feedContent(feedContent)
                .feedThemeTagList(feedThemeTagList)
                .feedMateTag(feedMateTag)
                .hiddenDayList(hiddenDayList)
                .hiddenPlaceList(hiddenPlaceList)
                .hiddenActivityList(hiddenActivityList)
                .hiddenItemList(hiddenItemList)
                .build();

        // 외부 api 호출을 신청한다?
        saveFeedPort.save(feed);
    }

}
