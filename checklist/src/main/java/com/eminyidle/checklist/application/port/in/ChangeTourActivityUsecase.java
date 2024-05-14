package com.eminyidle.checklist.application.port.in;

public interface ChangeTourActivityUsecase {
    //활동 생성
    void createActivity(String tourId, String PlaceId, Integer tourDay, String activityName);
    void createActivity(String tourPlaceId, String activityName);
    //활동 삭제
    void deleteActivity(String tourPlaceId, String activityName);
}
