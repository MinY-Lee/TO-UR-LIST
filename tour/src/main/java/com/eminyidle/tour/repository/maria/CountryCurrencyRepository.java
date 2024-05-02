package com.eminyidle.tour.repository.maria;

import com.eminyidle.tour.dto.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency,String> {
}
