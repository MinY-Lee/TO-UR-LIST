package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.req.PaymentInfoReq;

public interface PaymentService {
    ExchangeRate loadExchangeRate(String countryCode, String date);

    CountryCurrency loadCountryCurrency(String countryCode);

    String createPaymentInfo(PaymentInfoReq paymentInfo);
}
