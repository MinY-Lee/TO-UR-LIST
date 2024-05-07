package com.eminyidle.payment.controller;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.req.PayIdReq;
import com.eminyidle.payment.dto.req.PaymentInfoReq;
import com.eminyidle.payment.dto.res.ExchangeRateRes;
import com.eminyidle.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/pay")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/currency/{countryCode}/{date}")
    public ResponseEntity<?> getCountryCurrencyRate(@NotBlank @PathVariable("countryCode") String countryCode,
                                                    @NotBlank @PathVariable("date") String date) {

        log.info(countryCode);
        log.info(date);
        //날짜 파싱
        String parseDate = date.replace("-", "");

        ExchangeRate exchangeRate = paymentService.loadExchangeRate(countryCode, parseDate);
        CountryCurrency countryCurrency = paymentService.loadCountryCurrency(countryCode);

        return ResponseEntity.ok().body(
                ExchangeRateRes.builder()
                        .unit(countryCurrency.getCurrencySign())
                        .currency(exchangeRate.getExchangeRate())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentInfoReq paymentInfoReq) {

        log.debug(paymentInfoReq.toString());

        String payId = paymentService.createPaymentInfo(paymentInfoReq);
        // 저장 결과
        Map<String, String> responsePayment = new HashMap<>();
        responsePayment.put("payId", payId);
        return ResponseEntity.ok().body(
                responsePayment
        );
    }

    @PutMapping("/{payId}")
    public ResponseEntity<Void> updatePayment(@Valid @PathVariable("payId") String payId,
                                              @Valid @RequestBody PaymentInfoReq paymentInfoReq) {

        log.debug(paymentInfoReq.toString());
        paymentService.updatePaymentInfo(payId, paymentInfoReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{payId}")
    public ResponseEntity<Void> deletePayment(@Valid @PathVariable("payId") String payId,
                                              @Valid @RequestBody PayIdReq payIdReq) {

        log.debug(payId);
        paymentService.deletePaymentInfo(payId, payIdReq);
        return ResponseEntity.ok().build();
    }
}
