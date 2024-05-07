package com.eminyidle.place.place.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceInfo {
//    private String id;
//    private String placeName;
//    private String primaryType;
//    private double latitude;
//    private double longitude;
//    private String shortFormattedAddress;
//    private List<String> photos;
    private String placeId;
    private String placeName;
    private String placePrimaryType;
    private double placeLatitude;
    private double placeLongitude;
    private String placeAddress;
    private boolean placeOpenNow;
    private List<String> placeWeekdayDescriptions;
    private List<String> placePhotoList;
    // 추가 정보
}
