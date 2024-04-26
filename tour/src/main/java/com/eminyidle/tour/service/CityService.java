package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Country;
import com.eminyidle.tour.dto.CountryInfo;

import java.util.List;

public interface CityService {

    List<Country> searchCountryList();

//    List<Country> searchCountryListByKeyword(String keyword);

    List<City> searchCityList(String countryCode);

//    List<City> searchCityListByKeyword(String countryCode, String keyword);

    /**
     * 나라 별 기본정보 조회
     * @param countryCode
     * @return
     */
    CountryInfo searchCountryInfo(String countryCode);
}
