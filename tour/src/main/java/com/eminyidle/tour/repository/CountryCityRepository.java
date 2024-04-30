package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.CityEntity;
import com.eminyidle.tour.dto.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryCityRepository extends JpaRepository<CityEntity,String> {
    List<CityEntity> findAllByCountryCode(String countryCode);
}
