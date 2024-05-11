package com.eminyidle.checklist.service;

public interface EventListenService {
    //여행 생성
    void createTour();
    //여행 삭제
    void deleteTour();
    //나라 변경
    void updateCountry();


    //멤버 추가
    void createMember();
    //멤버 삭제
    void deleteMember();


    //장소 생성
    void createPlace();
    //장소 삭제
    void deletePlace();
    //장소 변경
    void updatePlace();


    //활동 생성
    void createActivity();
    //활동 삭제
    void deleteActivity();
}
