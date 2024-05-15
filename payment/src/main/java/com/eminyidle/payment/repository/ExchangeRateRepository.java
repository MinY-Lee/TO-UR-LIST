package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.ExchangeRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRateId> {
    Optional<ExchangeRate> findByExchangeRateId(ExchangeRateId exchangeRateId);

    List<ExchangeRate> findByExchangeRateIdDate(LocalDateTime date);
}
