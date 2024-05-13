package com.eminyidle.tour.country.repository;

import com.eminyidle.tour.country.dto.entity.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency,String> {
}
