package com.eminyidle.place.place.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

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
