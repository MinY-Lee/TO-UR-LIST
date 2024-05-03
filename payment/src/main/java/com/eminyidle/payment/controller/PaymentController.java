package com.eminyidle.payment.controller;

import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.res.ExchangeRateRes;
import com.eminyidle.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/pay")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/currency/{countryCode}/{date}")
    public ResponseEntity<?> getCountryCurrencyRate(@Valid @NotBlank @PathVariable("countryCode") String countryCode,
                                                    @Valid @NotBlank @PathVariable("date") String date) {

        log.info(countryCode);
        log.info(date);
        //날짜 파싱
        String parseDate = date.replace("-", "");

        ExchangeRate exchangeRate = paymentService.loadExchangeRate(countryCode, parseDate);

        return ResponseEntity.ok().body(
                ExchangeRateRes.builder()
                        .unit(exchangeRate.getCountryCurrency().getCurrencySign())
                        .currency(exchangeRate.getExchangeRate())
                        .build()
        );
    }
}
