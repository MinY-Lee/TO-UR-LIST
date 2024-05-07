package com.eminyidle.place.place.dto;

import com.eminyidle.place.place.dto.node.TourPlace;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Do {
    @Id
    @GeneratedValue
    private String id;
    @Property
    private String doId;
    @Property
    private String placeId;
    @Property
    private String placeName;
    @Property
    private Integer tourDay;

    @TargetNode
    private TourPlace tourPlace;

}
