package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.ExchangeRateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExchangeRateRepositoryTest {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Test
    @DisplayName("환율 데이터 찾기 - 성공")
    @Order(1)
    void findByExchangeRateIdTrueCase() {
        //given
        // 11시 05분 기준
        LocalDateTime now = LocalDateTime.now();
        now = now.withHour(11).withMinute(5).withSecond(0).withNano(0);
        ExchangeRateId exchangeRateId = ExchangeRateId.builder()
                .currencyCode("KRW")
                .date(now)
                .build();

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .exchangeRateId(exchangeRateId)
                .exchangeRate(1.0)
                .build();

        exchangeRateRepository.save(exchangeRate);

        // when
        ExchangeRate resultExchange = exchangeRateRepository.findByExchangeRateId(exchangeRateId).get();

        // then
        Assertions.assertThat(resultExchange.getExchangeRateId().getCurrencyCode()).isEqualTo("KRW");
        Assertions.assertThat(resultExchange.getExchangeRateId().getDate()).isEqualTo(now);
        Assertions.assertThat(resultExchange.getExchangeRate()).isEqualTo(1.0);
    }
}