package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.*;
import com.eminyidle.tour.dto.entity.CountryCurrency;
import com.eminyidle.tour.dto.entity.CountryEntity;
import com.eminyidle.tour.exception.NoSuchCountryException;
import com.eminyidle.tour.repository.maria.CountryCityRepository;
import com.eminyidle.tour.repository.maria.CountryCurrencyRepository;
import com.eminyidle.tour.repository.maria.CountryRepository;
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

    @Override
    public CountryInfo searchCountryInfo(String countryCode) {
        CountryEntity country = countryRepository.findById(countryCode).orElseThrow(NoSuchCountryException::new);
        CountryCurrency currency=countryCurrencyRepository.findById(countryCode).orElseThrow(NoSuchCountryException::new);

        return CountryInfo.builder()
                .KST(country.getUtc()-9) //KOR utc기준(=9)와의 차이
                .climate(country.getClimate())
                .currencyUnit(currency.getCurrencySign())
                .language(country.getLanguage())
                .plug_type(country.getPlug_type())
                .voltage(country.getVoltage())
                .build();
    }
}
