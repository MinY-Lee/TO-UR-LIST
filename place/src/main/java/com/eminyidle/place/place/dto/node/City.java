package com.eminyidle.place.place.dto.node;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("CITY")
public class City {

    @Id
    @Property("cityId")
    private Integer cityId;

    @Property("cityName")
    private String cityName;

    @Relationship(type = "IN", direction = OUTGOING)
    private Country country;
}
