package com.eminyidle.checklist.application.port.in;

import java.util.Set;

public interface ChangeTourUsecase {
    //여행 생성
    void createTour(String tourId, Long tourPeriod, Set<String> countryCodeSet) ;
    //여행 삭제
    void deleteTour(String tourId);
    //나라 변경
    void updateCountry(String tourId, Set<String> countryCodeSet);
}
