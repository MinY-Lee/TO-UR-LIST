package com.eminyidle.tour.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@Builder
public class TourCity {

    private String countryCode;
    private String cityName;
}
