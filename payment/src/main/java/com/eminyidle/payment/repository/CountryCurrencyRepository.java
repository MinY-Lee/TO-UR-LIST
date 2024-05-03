package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency, String> {
    Optional<CountryCurrency> findByCountryCode(String countryCode);
}
