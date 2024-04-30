package com.eminyidle.place.place.dto;

import com.eminyidle.place.place.dto.node.TourActivity;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Do {
    @Id
    @GeneratedValue
    private String id;
    @Property
    private String placeId;
    @Property
    private String placeName;
    @Property
    private Integer tourDay;

    @TargetNode
    private TourActivity tourActivity;

}
