package com.eminyidle.feed.domain;

import com.eminyidle.feed.adapter.dto.HiddenActivity;
import com.eminyidle.feed.adapter.dto.HiddenItem;
import com.eminyidle.feed.adapter.dto.HiddenPlace;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Feed {
    private String feedId;
    private String feedTitle;
    private String feedContent;
    private List<String> feedThemeTagList;
    private String feedMateTag;
//    private String tourId;
    private List<Integer> hiddenDayList;
    private List<HiddenPlace> hiddenPlaceList;
    private List<HiddenActivity> hiddenActivityList;
    private List<HiddenItem> hiddenItemList;
}
