package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.CountryCurrencyId;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.ExchangeRateId;
import com.eminyidle.payment.repository.CountryCurrencyRepository;
import com.eminyidle.payment.repository.ExchangeRateRepository;
import com.eminyidle.payment.repository.PaymentInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PaymentServiceTest {

    @Mock
    private CountryCurrencyRepository countryCurrencyRepository;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private PaymentInfoRepository paymentInfoRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("환율정보 불러오기 Mock 사용 - 성공")
    @Order(1)
    void loadExchangeRate() {
        //given 입력값 리턴값
        String countryCode = "KOR", currencyCode = "KRW", currencySign = "₩";
        String date = "20240513";

        CountryCurrencyId countryCurrencyId = CountryCurrencyId.builder()
                .countryCode(countryCode)
                .currencyCode(currencyCode)
                .build();
        CountryCurrency countryCurrency = CountryCurrency.builder()
                .countryCurrencyId(countryCurrencyId)
                .currencySign(currencySign)
                .build();

        List<CountryCurrency> currencyList = new ArrayList<>();
        currencyList.add(countryCurrency);


        // 날짜 정보
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate datePart = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = datePart.atTime(11, 5, 0, 0);

        ExchangeRateId exchangeRateId = ExchangeRateId.builder()
                .currencyCode(currencyCode)
                .date(dateTime)
                .build();
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .exchangeRateId(exchangeRateId)
                .exchangeRate(1.0)
                .build();

        // 통화코드 찾기
        when(countryCurrencyRepository.findByCountryCurrencyIdCountryCode(any(String.class)))
                .thenReturn(currencyList);

        when(exchangeRateRepository.findByExchangeRateId(any(ExchangeRateId.class)))
                .thenReturn(Optional.of(exchangeRate));

        //when
        ExchangeRate result = paymentService.loadExchangeRate(countryCode, date);

        //then
        Assertions.assertThat(result.getExchangeRateId().getCurrencyCode()).isEqualTo(exchangeRateId.getCurrencyCode());
        Assertions.assertThat(result.getExchangeRateId().getDate()).isEqualTo(exchangeRateId.getDate());
        Assertions.assertThat(result.getExchangeRate()).isEqualTo(1.0);

        verify(countryCurrencyRepository, times(1)).findByCountryCurrencyIdCountryCode(any(String.class));
        verify(exchangeRateRepository, times(1)).findByExchangeRateId(any(ExchangeRateId.class));
    }

    @Test
    void loadCountryCurrency() {
    }

    @Test
    void createPaymentInfo() {
    }

    @Test
    void updatePaymentInfo() {
    }

    @Test
    void deletePaymentInfo() {
    }

    @Test
    void searchPaymentInfoList() {
    }

    @Test
    void searchPaymentInfo() {
    }

    @Test
    void updatePaymentUserId() {
    }
}