package com.eminyidle.place.place.dto;

import jakarta.persistence.Entity;
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
@Node("city")
public class City {

    @Id
    @Property("city_id")
    private Integer cityId;

    @Property("city_name")
    private String cityName;

    @Relationship(type = "IN", direction = OUTGOING)
    private Country country;
}
