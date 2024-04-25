package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Node
public class City {
    @Id @GeneratedValue
    private Long id;
//    @Relationship(type = "IN")
    private String countryCode;
    @Property
    private String cityName;


}
