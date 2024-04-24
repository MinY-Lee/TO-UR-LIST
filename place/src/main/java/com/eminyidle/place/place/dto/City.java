package com.eminyidle.place.place.dto;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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
