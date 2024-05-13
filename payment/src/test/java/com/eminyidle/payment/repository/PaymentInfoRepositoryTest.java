package com.eminyidle.payment.repository;


import com.eminyidle.payment.config.MongoDBInit;
import com.eminyidle.payment.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

@MongoDBInit
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentInfoRepositoryTest {

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Test
    @DisplayName("몽고 디비 데이터 저장 - 성공")
    void saveMongoDataTest() {
        // given
        PaymentMember paymentMember = PaymentMember.builder()
                .payAmount(1000)
                .userId("123123")
                .build();

        List<PaymentMember> paymentMemberList = new ArrayList<>();
        paymentMemberList.add(paymentMember);

        String publicKey = UUID.randomUUID().toString();
        String privateKey = UUID.randomUUID().toString();

        LocalDateTime time = LocalDateTime.now().withNano(0);

        PublicPayment publicPayment = PublicPayment.builder()
                .payAmount(1000)
                .unit("$")
                .currencyCode("USD")
                .payMethod("현금")
                .payDatetime(time)
                .payContent("관광비")
                .payCategory("관광")
                .payerId("123123")
                .payMemberList(paymentMemberList)
                .build();

        PrivatePaymentInfo privatePaymentInfo = PrivatePaymentInfo.builder()
                .privatePaymentId(privateKey)
                .payAmount(1001)
                .unit("₩")
                .currencyCode("KRW")
                .payMethod("카드")
                .payDatetime(LocalDateTime.now().minusDays(1))
                .payContent("저녁 먹기")
                .payCategory("음식점")
                .build();

        List<PrivatePaymentInfo> privatePaymentList = new ArrayList<>();
        privatePaymentList.add(privatePaymentInfo);

        List<String> publicPaymentList = new ArrayList<>();
        publicPaymentList.add(publicKey);

        PrivatePayment privatePayment = PrivatePayment.builder()
                .privatePaymentList(privatePaymentList)
                .publicPaymentList(publicPaymentList)
                .build();

        Map<String,PublicPayment> publicPaymentMap = new LinkedHashMap<>();
        Map<String,PrivatePayment> privatePaymentMap = new LinkedHashMap<>();

        publicPaymentMap.put(publicKey, publicPayment);
        privatePaymentMap.put(privateKey, privatePayment);

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .id("12345")
                .publicPayment(publicPaymentMap)
                .privatePayment(privatePaymentMap)
                .build();
        // when
        PaymentInfo savePayment = paymentInfoRepository.save(paymentInfo);

        // then
        PublicPayment dbPublicPayment = savePayment.getPublicPayment().get(publicKey);

        Assertions.assertThat(dbPublicPayment.getPayAmount()).isEqualTo(1000);
        Assertions.assertThat(dbPublicPayment.getUnit()).isEqualTo("$");
        Assertions.assertThat(dbPublicPayment.getCurrencyCode()).isEqualTo("USD");
        Assertions.assertThat(dbPublicPayment.getPayMethod()).isEqualTo("현금");
        Assertions.assertThat(dbPublicPayment.getPayDatetime()).isEqualTo(time);
        Assertions.assertThat(dbPublicPayment.getPayContent()).isEqualTo("관광비");
        Assertions.assertThat(dbPublicPayment.getPayerId()).isEqualTo("123123");
        Assertions.assertThat(dbPublicPayment.getPayMemberList().size()).isEqualTo(1);
    }

}