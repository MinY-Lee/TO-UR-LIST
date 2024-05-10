package com.eminyidle.feed.adapter.out.persistence;

import com.eminyidle.feed.adapter.dto.FeedEntity;
import com.eminyidle.feed.domain.Feed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {

    public FeedEntity toEntity(Feed feed) {
        return FeedEntity.builder()
                .feedId(feed.getFeedId())
                .feedTitle(feed.getFeedTitle())
                .feedContent(feed.getFeedContent())
                .feedThemeTagList(feed.getFeedThemeTagList())
                .feedMateTag(feed.getFeedMateTag())
                .hiddenDayList(feed.getHiddenDayList())
                .hiddenPlaceList(feed.getHiddenPlaceList().stream().map(hiddenPlace -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.writeValueAsString(hiddenPlace);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                }).toList())
                .hiddenActivityList(feed.getHiddenActivityList().stream().map(hiddenActivity -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.writeValueAsString(hiddenActivity);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                }).toList())
                .hiddenItemList(feed.getHiddenItemList().stream().map(hiddenItem -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.writeValueAsString(hiddenItem);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                }).toList())
                .build();
    }
}
