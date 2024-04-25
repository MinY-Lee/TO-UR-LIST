package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;


@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Tour {
    //제목, 기간, 도시
    @Id
    String tourId;
    String tourTitle;
    @Property(readOnly = true)
    LocalDateTime startDate;
    @Property
    LocalDateTime endDate;
    @Relationship(type = "TO")
    List<City> cityList;
}
