package com.eminyidle.place.place.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceList {
    // Place에서 받아 온 객체를 리스트로 받는 부분
    private List<Place> places;
}
