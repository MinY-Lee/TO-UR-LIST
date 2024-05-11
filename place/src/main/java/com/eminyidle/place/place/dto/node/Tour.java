package com.eminyidle.place.place.dto.node;

import com.eminyidle.place.place.dto.Do;
import com.eminyidle.place.place.dto.node.City;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("TOUR")
@ToString
public class Tour {
    @Id
    private String tourId;
    private String tourTitle;
    private String startDate;
    private String endDate;
    List<City> cityList;

    @Relationship(type = "DO", direction = OUTGOING)    // 관계 설정
    private List<Do> placeList;
}
