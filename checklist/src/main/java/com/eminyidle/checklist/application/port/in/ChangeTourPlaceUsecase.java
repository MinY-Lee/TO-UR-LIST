package com.eminyidle.checklist.application.port.in;

public interface ChangeTourPlaceUsecase {
    //장소 생성
    void createPlace(String tourId, String tourPlaceId, String placeId, Integer tourDay, String placeName);

    //장소 삭제
    void deletePlace(String tourId, String placeId, Integer tourDay);

    //장소 변경
    void updatePlace(String tourId, String tourPlaceId, String placeId, Integer tourDayAfter);
}
