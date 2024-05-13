package com.eminyidle.feed.adapter.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document
public class FeedTourEntity {
    @Id
    private String feedId;

    private String feedTitle;
    private String feedContent;
    private String userNickname;
    private List<String> feedThemeTagList;
    private String feedMateTag;
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<City> cityList;
    private List<Place> placeList;
    private List<Item> itemList;
    private Integer copyCount;
    private Integer likeCount;
    private Boolean isLiked;
}
