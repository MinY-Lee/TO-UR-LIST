package com.eminyidle.checklist.service;

public class EventListnerServiceImpl implements EventListenService{

    @Override
    public void createTour() {
        // tour 생성
            //updateCountry()
        // 장소 없음 tour_place 생성
        // 활동 없음 tour_activity 생성
    }

    @Override
    public void deleteTour() {
        // tour와 관련된 모든 tour_activity 삭제
        // 모든 tour_place 삭제
        // tour 삭제
    }

    @Override
    public void updateCountry() {
        //이전 country로 연결되어있는 모든 관계 끊고
        //새로운 country로 연결된 모든 관계 연결
            // common은 냅두고
            // 다른 것들만... 끊고 추가한다
    }

    @Override
    public void createMember() {
        //public 연결이 있는 모든 것들 찾아서.. private 그 유저아이디 연결
    }

    @Override
    public void deleteMember() {
        //모든 private 그 userId 삭제
    }

    @Override
    public void createPlace() {
        // 맞는 tour 확인 -> 없음 에러
        // tour_place 생성
        // 그에 맞게 활동 없음 tour_activity 생성
    }

    @Override
    public void deletePlace() {
        // tour-place와 관련된 모든 tour_activity 삭제
        // tour_place 삭제
    }

    @Override
    public void updatePlace() { //장소 날짜 수정
        //TODO: 잘 합쳐 줘야 함..
        //해당 날짜에 있는 활동과, 그 장소가 가진 활동들 잘 합쳐 줘야 함
    }

    @Override
    public void createActivity() {
        // tour의 tour_place 확인
        // tour_activity 생성
    }

    @Override
    public void deleteActivity() {
        // tour_activity 삭제
    }
}
