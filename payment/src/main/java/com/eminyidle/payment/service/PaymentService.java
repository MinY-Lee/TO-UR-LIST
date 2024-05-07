package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.req.PayIdReq;
import com.eminyidle.payment.dto.req.PaymentInfoReq;
import com.eminyidle.payment.dto.res.PaymentInfoRes;

import java.util.List;

public interface PaymentService {
    ExchangeRate loadExchangeRate(String countryCode, String date);

    CountryCurrency loadCountryCurrency(String countryCode);

    String createPaymentInfo(PaymentInfoReq paymentInfo, String userId);

    void updatePaymentInfo(String payId, PaymentInfoReq paymentInfo, String userId);

    void deletePaymentInfo(String payId, PayIdReq payIdReq, String userId);

    List<PaymentInfoRes> searchPaymentInfoList(String tourId, String userId);

    PaymentInfoRes searchPaymentInfo(String payId, PayIdReq payIdReq, String userId);


}
