package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Country;
import com.eminyidle.tour.dto.CountryEntity;
import com.eminyidle.tour.dto.CountryInfo;
import com.eminyidle.tour.exception.NoSuchCountryException;
import com.eminyidle.tour.repository.CountryCityRepository;
import com.eminyidle.tour.repository.CountryCurrencyRepository;
import com.eminyidle.tour.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryCurrencyRepository countryCurrencyRepository;
    private final CountryCityRepository cityRepository;

    @Override
    public List<Country> searchCountryList() {

        return countryRepository.findAll().stream().map(countryEntity ->
                Country.builder()
                        .countryCode(countryEntity.getCountryCode())
                        .countryName(countryEntity.getCountryNameKor())
                        .build()
        ).toList();
    }

    @Override
    public List<City> searchCityList(String countryCode) {
        return cityRepository.findAllByCountryCode(countryCode).stream().map(
                cityEntity -> City.builder() //TODO - cityID는 마리아디비에서 챙겨야 할까?
                        .cityName(cityEntity.getCityNameKor())
                        .countryCode(cityEntity.getCountryCode())
                        .build()
        ).toList();
    }

    // TODO - currency 어떤 정보를 줄 건지!
    @Override
    public CountryInfo searchCountryInfo(String countryCode) {
        CountryEntity country = countryRepository.findById(countryCode).orElseThrow(NoSuchCountryException::new);
        countryCurrencyRepository.findById(countryCode).orElseThrow(NoSuchCountryException::new);
        return null;
    }
}
