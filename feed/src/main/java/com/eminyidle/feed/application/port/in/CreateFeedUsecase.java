package com.eminyidle.feed.application.port.in;

import com.eminyidle.feed.adapter.dto.HiddenActivity;
import com.eminyidle.feed.adapter.dto.HiddenItem;
import com.eminyidle.feed.adapter.dto.HiddenPlace;

import java.util.List;

public interface CreateFeedUsecase {
    void createFeed(String feedId, String feedTitle, String feedContent, List<String> feedThemeTagList, String feedMateTag,
                    String tourId, List<Integer> hiddenDayList, List<HiddenPlace> hiddenPlaceList,
                    List<HiddenActivity> hiddenActivityList, List<HiddenItem> hiddenItemList);
}
