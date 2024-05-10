package com.eminyidle.feed.adapter.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node("FEED")
public class FeedEntity {
    @Id
    private String feedId;
    private String feedTitle;
    private String feedContent;
    private List<String> feedThemeTagList;
    private String feedMateTag;
//    private String tourId;
    private List<Integer> hiddenDayList;
    @Property
//    private List<Object> hiddenPlaceList;
    private List<String> hiddenPlaceList;
    @Property
    private List<String> hiddenActivityList;
    @Property
    private List<String> hiddenItemList;
}
