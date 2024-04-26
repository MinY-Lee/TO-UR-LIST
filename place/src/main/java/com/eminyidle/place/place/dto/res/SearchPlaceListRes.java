package com.eminyidle.place.place.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPlaceListRes {
    // Controller로 전해줄 부분
    private String placeId;
    private String placeName;
    private String placePrimaryType;
    private List<String> placePhotoList;
}
