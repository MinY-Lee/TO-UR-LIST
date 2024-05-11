package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.*;
import com.eminyidle.payment.dto.req.PayIdReq;
import com.eminyidle.payment.dto.req.PaymentInfoReq;
import com.eminyidle.payment.dto.res.PaymentInfoRes;
import com.eminyidle.payment.exception.CurrencyNotExistException;
import com.eminyidle.payment.exception.ExchangeRateNotExistException;
import com.eminyidle.payment.exception.PaymentNotExistException;
import com.eminyidle.payment.repository.CountryCurrencyRepository;
import com.eminyidle.payment.repository.ExchangeRateRepository;
import com.eminyidle.payment.repository.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final CountryCurrencyRepository countryCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    // 환율 조회
    @Override
    public ExchangeRate loadExchangeRate(String countryCode, String date) {

        // 통화코드 찾기
        List<CountryCurrency> currencyList = countryCurrencyRepository.findByCountryCurrencyIdCountryCode(countryCode);

        if (currencyList.isEmpty()) {
            throw new CurrencyNotExistException("해당하는 통화코드가 없습니다.");
        }
        CountryCurrency countryCurrency = currencyList.get(0);

        // 날짜 정보
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate datePart = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = datePart.atTime(11, 5, 0);
        // 환율 가져오기
        return exchangeRateRepository.findByExchangeRateId(new ExchangeRateId(countryCurrency.getCountryCurrencyId().getCurrencyCode(), dateTime))
                .orElseThrow(() -> new ExchangeRateNotExistException("해당하는 환율데이터가 없습니다."));
    }

    // 통화코드 조회
    @Override
    public CountryCurrency loadCountryCurrency(String countryCode) {

        // 화폐정보 찾기
        List<CountryCurrency> currencyList = countryCurrencyRepository.findByCountryCurrencyIdCountryCode(countryCode);

        if (currencyList.isEmpty()) {
            throw new CurrencyNotExistException("해당하는 통화코드가 없습니다.");
        }

        return currencyList.get(0);
    }

    // 가계부 저장
    @Override
    public String createPaymentInfo(PaymentInfoReq paymentInfo, String userId) {
        String payId = null;

        // tourId로 기존 데이터 가져오기, 없으면 생성
        PaymentInfo payment = paymentInfoRepository.findById(paymentInfo.getTourId())
                .orElseGet(() -> PaymentInfo.builder()
                        .id(paymentInfo.getTourId())
                        .publicPayment(new LinkedHashMap<>())
                        .privatePayment(new LinkedHashMap<>())
                        .build());

        switch (paymentInfo.getPayType()) {
            // 공동 지출
            case ("public"): {
                String publicPaymentId = UUID.randomUUID().toString();
                PublicPayment publicPayment = makePublicPayment(paymentInfo);

                // 반영
                Map<String, PublicPayment> publicPaymentMap = payment.getPublicPayment();
                publicPaymentMap.put(publicPaymentId, publicPayment);
                payment.setPublicPayment(publicPaymentMap);

                // 개인 지출 유무
                if (!payment.getPrivatePayment().containsKey(userId)) {
                    // 개인 지출 항목 생성
                    ArrayList<String> publicPaymentList = new ArrayList<>();
                    publicPaymentList.add(publicPaymentId);

                    PrivatePayment updatePrivatePayment = PrivatePayment.builder()
                            .privatePaymentList(new ArrayList<>())
                            .publicPaymentList(publicPaymentList)
                            .build();
                    // 반영
                    Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                    privatePayment.put(userId, updatePrivatePayment);
                    payment.setPrivatePayment(privatePayment);
                } else {
                    // 기존 항목에 추가
                    Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                    List<String> publicPaymentList = privatePayment.get(userId).getPublicPaymentList();
                    publicPaymentList.add(publicPaymentId);
                    payment.setPrivatePayment(privatePayment);
                }
                payId = publicPaymentId;
                break;
            }
            // 개인 지출
            case ("private"): {
                // 개인 지출 항목 생성
                String privatePaymentId = UUID.randomUUID().toString();
                PrivatePaymentInfo privatePaymentInfo = makePrivatePayment(privatePaymentId, paymentInfo);

                // 개인 지출 유무
                if (!payment.getPrivatePayment().containsKey(userId)) {
                    // 개인 지출
                    ArrayList<PrivatePaymentInfo> paymentInfoArrayList = new ArrayList<>();
                    paymentInfoArrayList.add(privatePaymentInfo);

                    PrivatePayment updatePrivatePayment = PrivatePayment.builder()
                            .privatePaymentList(paymentInfoArrayList)
                            .publicPaymentList(new ArrayList<>())
                            .build();
                    // 반영
                    Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                    privatePayment.put(userId, updatePrivatePayment);
                    payment.setPrivatePayment(privatePayment);
                } else {
                    // 기존 항목에 추가
                    Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                    List<PrivatePaymentInfo> privatePaymentList = privatePayment.get(userId).getPrivatePaymentList();
                    privatePaymentList.add(privatePaymentInfo);
                    payment.setPrivatePayment(privatePayment);
                }
                payId = privatePaymentId;
                break;
            }
        }
        // 디비에 저장
        paymentInfoRepository.save(payment);
        return payId;
    }

    @Override
    public void updatePaymentInfo(String payId, PaymentInfoReq paymentInfo, String userId) {

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(paymentInfo.getTourId())
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        switch (paymentInfo.getPayType()) {
            // 공동 지출 내용만 변경
            case ("public"): {
                if (!payment.getPublicPayment().containsKey(payId)) {
                    throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
                }
                PublicPayment updatePublicPayment = makePublicPayment(paymentInfo);

                //payId에 해당하는 공동 지출 내역 변경 및 반영
                payment.getPublicPayment().replace(payId, updatePublicPayment);
                break;
            }
            // 개인 지출
            case ("private"): {
                // 개인 지출 항목 생성
                PrivatePaymentInfo updatePrivatePaymentInfo = makePrivatePayment(payId, paymentInfo);

                // 기존 항목에서 변경된 내용 추가
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                List<PrivatePaymentInfo> privatePaymentList = privatePayment.get(userId).getPrivatePaymentList();

                boolean flag = false;
                int listIdx = 0;
                for (PrivatePaymentInfo currPrivatePaymentInfo : privatePaymentList) {
                    if (currPrivatePaymentInfo.getPrivatePaymentId().equals(payId)) {
                        flag = true;
                        break;
                    }
                    listIdx++;
                }
                // 없는 경우
                if (!flag) {
                    throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
                }
                privatePaymentList.set(listIdx, updatePrivatePaymentInfo);
                break;
            }
        }
        // 디비에 저장
        paymentInfoRepository.save(payment);
    }

    @Override
    public void deletePaymentInfo(String payId, PayIdReq payIdReq, String userId) {
        // 투어 ID
        String tourId = payIdReq.getTourId();

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(tourId)
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        switch (payIdReq.getPayType()) {
            case "public": {
                // payId로 해당 지출 내역 찾기 - 공통
                if (payment.getPublicPayment().containsKey(payId)) {
                    // 해당하는 지출 제거
                    PublicPayment removedPayment = payment.getPublicPayment().remove(payId);
                    log.debug(removedPayment.toString());

                    // 개인지출의 public 리스트에서도 제거
                    List<String> publicPaymentList = payment.getPrivatePayment().get(userId).getPublicPaymentList();

                    int listIdx = 0;
                    for (String currPublicPaymentId : publicPaymentList) {
                        if (currPublicPaymentId.equals(payId)) {
                            break;
                        }
                        listIdx++;
                    }
                    String removePaymentId = publicPaymentList.remove(listIdx);
                    log.debug(removePaymentId);
                } else {
                    throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
                }
                break;
            }
            case "private": {
                // 리스트에서 탐색
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                List<PrivatePaymentInfo> privatePaymentList = privatePayment.get(userId).getPrivatePaymentList();

                boolean flag = false;
                int listIdx = 0;
                for (PrivatePaymentInfo currPrivatePaymentInfo : privatePaymentList) {
                    if (currPrivatePaymentInfo.getPrivatePaymentId().equals(payId)) {
                        flag = true;
                        break;
                    }
                    listIdx++;
                }

                // 없는 경우
                if (!flag) {
                    throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
                }

                PrivatePaymentInfo removePayment = privatePaymentList.remove(listIdx);
                log.debug(removePayment.toString());
                break;
            }
        }
        // 디비에 저장
        paymentInfoRepository.save(payment);
    }

    @Override
    public List<PaymentInfoRes> searchPaymentInfoList(String tourId, String userId) {
        List<PaymentInfoRes> result = new ArrayList<>();

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(tourId)
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        // 개인 지출 가져오기
        Map<String, PrivatePayment> privatePaymentMap = payment.getPrivatePayment();
        if (!privatePaymentMap.containsKey(userId)) {
            return result;
        }
        // 개인 지출, 공동 지출
        PrivatePayment privatePayment = privatePaymentMap.get(userId);

        // 개인 지출 리스트
        List<PrivatePaymentInfo> privatePaymentList = privatePayment.getPrivatePaymentList();

        // 개인 지출 반영
        for (PrivatePaymentInfo currPrivatePayment : privatePaymentList) {
            PaymentInfoRes paymentInfoRes = makePaymentInfoRes(tourId, userId, currPrivatePayment);
            result.add(paymentInfoRes);
        }

        // 공동 지출 가져오기
        Map<String, PublicPayment> publicPaymentMap = payment.getPublicPayment();
        List<String> publicPaymentList = privatePayment.getPublicPaymentList();

        for (String publicPaymentId : publicPaymentList) {
            if (!publicPaymentMap.containsKey(publicPaymentId)) {
                throw new PaymentNotExistException("해당하는 지출 정보가 없습니다.");
            }
            PublicPayment publicPayment = publicPaymentMap.get(publicPaymentId);
            PaymentInfoRes paymentInfoRes = makePaymentInfoRes(tourId, publicPaymentId, publicPayment);
            result.add(paymentInfoRes);
        }
        return result;
    }

    @Override
    public PaymentInfoRes searchPaymentInfo(String payId, PayIdReq payIdReq, String userId) {
        // 투어 ID
        String tourId = payIdReq.getTourId();

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(tourId)
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        switch (payIdReq.getPayType()) {
            case "public": {
                // payId로 해당 지출 내역 찾기 - 공통
                if (payment.getPublicPayment().containsKey(payId)) {
                    // 해당하는 정보 반환
                    PublicPayment publicPayment = payment.getPublicPayment().get(payId);
                    log.debug(publicPayment.toString());
                    return makePaymentInfoRes(tourId, payId, publicPayment);
                } else {
                    throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
                }
            }
            case "private": {
                // 리스트에서 탐색
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                List<PrivatePaymentInfo> privatePaymentList = privatePayment.get(userId).getPrivatePaymentList();

                for (PrivatePaymentInfo currPrivatePaymentInfo : privatePaymentList) {
                    if (currPrivatePaymentInfo.getPrivatePaymentId().equals(payId)) {
                        log.debug(currPrivatePaymentInfo.toString());
                        return makePaymentInfoRes(tourId, payId, currPrivatePaymentInfo);
                    }
                }
                break;
            }
        }
        // 없는 경우
        throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
    }

    @Override
    public void updatePaymentUserId(String tourId, Message message) {
        String ghostId = message.getGhostId();
        String userId = message.getUserId();

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(tourId)
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        Map<String, PublicPayment> publicPaymentMap = payment.getPublicPayment();
        Map<String, PrivatePayment> privatePaymentMap = payment.getPrivatePayment();
        PrivatePayment privatePayment = privatePaymentMap.get(message.getGhostId());
        if(privatePayment == null) {
            throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
        }

        // publicPayment의 ghostId 변경
        List<String> publicPaymentList = privatePayment.getPublicPaymentList();

        for(String payId : publicPaymentList) {
            if(publicPaymentMap.containsKey(payId)) {
                PublicPayment publicPayment = publicPaymentMap.get(payId);
                // 공동 구매내역의 결제자 고스트 유저 변경
                if(publicPayment.getPayerId().equals(ghostId)) {
                    publicPayment.setPayerId(userId);
                }
                // 공동 구매내역의 고스트 유저 변경
                List<PaymentMember> payMemberList = publicPayment.getPayMemberList();
                for(PaymentMember member : payMemberList) {
                    if(member.getUserId().equals(ghostId)) {
                        member.setUserId(userId);
                    }
                }
            }
        }

        // private의 ghostId 변경
        privatePaymentMap.put(userId, privatePayment);
        privatePaymentMap.remove(ghostId);

        // 디비에 저장
        paymentInfoRepository.save(payment);
    }

    private PublicPayment makePublicPayment(PaymentInfoReq paymentInfo) {
        return PublicPayment.builder()
                .payAmount(paymentInfo.getPayAmount())
                .unit(paymentInfo.getUnit())
                .currencyCode(paymentInfo.getCurrencyCode())
                .payMethod(paymentInfo.getPayMethod())
                .payDatetime(paymentInfo.getPayDatetime())
                .payContent(paymentInfo.getPayContent())
                .payCategory(paymentInfo.getPayCategory())
                .payerId(paymentInfo.getPayerId())
                .payMemberList(paymentInfo.getPayMemberList())
                .build();
    }

    private PrivatePaymentInfo makePrivatePayment(String payId, PaymentInfoReq paymentInfo) {
        return PrivatePaymentInfo.builder()
                .privatePaymentId(payId)
                .payAmount(paymentInfo.getPayAmount())
                .unit(paymentInfo.getUnit())
                .currencyCode(paymentInfo.getCurrencyCode())
                .payMethod(paymentInfo.getPayMethod())
                .payDatetime(paymentInfo.getPayDatetime())
                .payContent(paymentInfo.getPayContent())
                .payCategory(paymentInfo.getPayCategory())
                .build();
    }

    private PaymentInfoRes makePaymentInfoRes(String tourId, String publicPaymentId, PublicPayment publicPayment) {
        return PaymentInfoRes.builder()
                .payId(publicPaymentId)
                .payType("public")
                .tourId(tourId)
                .payAmount(publicPayment.getPayAmount())
                .unit(publicPayment.getUnit())
                .currencyCode(publicPayment.getCurrencyCode())
                .payMethod(publicPayment.getPayMethod())
                .payDatetime(publicPayment.getPayDatetime())
                .payContent(publicPayment.getPayContent())
                .payCategory(publicPayment.getPayCategory())
                .payerId(publicPayment.getPayerId())
                .payMemberList(publicPayment.getPayMemberList())
                .build();
    }

    private PaymentInfoRes makePaymentInfoRes(String tourId, String userId, PrivatePaymentInfo currPrivatePayment) {
        return PaymentInfoRes.builder()
                .payId(currPrivatePayment.getPrivatePaymentId())
                .payType("private")
                .tourId(tourId)
                .payAmount(currPrivatePayment.getPayAmount())
                .unit(currPrivatePayment.getUnit())
                .currencyCode(currPrivatePayment.getCurrencyCode())
                .payMethod(currPrivatePayment.getPayMethod())
                .payDatetime(currPrivatePayment.getPayDatetime())
                .payContent(currPrivatePayment.getPayContent())
                .payCategory(currPrivatePayment.getPayCategory())
                .payerId(userId)
                .payMemberList(new ArrayList<>())
                .build();
    }
}
