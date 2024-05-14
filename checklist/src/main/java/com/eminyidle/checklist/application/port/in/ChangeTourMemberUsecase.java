package com.eminyidle.checklist.application.port.in;

public interface ChangeTourMemberUsecase {
    //멤버 추가
    void createMember(String tourId, String userId);
    //멤버 삭제
    void deleteMember(String tourId, String userId);
}
