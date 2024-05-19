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
import java.util.stream.Collectors;


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
        LocalDateTime dateTime = datePart.atTime(11, 5, 0, 0);
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
                payId = makePublicPayment(paymentInfo, payment);
                break;
            }
            // 개인 지출
            case ("private"): {
                // 개인 지출 항목 생성
                PrivatePaymentInfo privatePaymentInfo = makePrivatePaymentInfo(paymentInfo, userId, payment);
                payId = privatePaymentInfo.getPrivatePaymentId();
                break;
            }
        }
        // 디비에 저장
        paymentInfoRepository.save(payment);
        return payId;
    }

    @Override
    public void updatePaymentInfo(String payId, PaymentInfoReq paymentInfo, String userId) {
        boolean publicFlag = false;
        boolean privateFlag = false;

        // tourId로 기존 데이터 가져오기
        PaymentInfo payment = paymentInfoRepository.findById(paymentInfo.getTourId())
                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        // 공동 지출내용 변경
        if (payment.getPublicPayment().containsKey(payId)) {
            publicFlag = true;
            // 공동 지출에서 공동 지출 내용 변경
            if (paymentInfo.getPayType().equals("public")) {
                PublicPayment updatePublicPayment = makePublicPayment(paymentInfo);

                // 기존 멤버 추출
                Set<String> prevMember = payment.getPublicPayment().get(payId).getPayMemberList().stream()
                        .map(PaymentMember::getUserId)
                        .collect(Collectors.toSet());

                log.debug(prevMember.toString());

                // 새로운 멤버 추출
                Set<String> newMember = paymentInfo.getPayMemberList().stream()
                        .map(PaymentMember::getUserId)
                        .collect(Collectors.toSet());

                log.debug(prevMember.toString());

                // 빠진 사람: 기존 멤버에서 새로운 멤버를 뺀 차집합
                Set<String> removedMembers = new HashSet<>(prevMember);
                removedMembers.removeAll(newMember);

                // 추가된 사람: 새로운 멤버에서 기존 멤버를 뺀 차집합
                Set<String> addedMembers = new HashSet<>(newMember);
                addedMembers.removeAll(prevMember);

                // `privatePayment`의 `publicPaymentList` 업데이트
                for (Map.Entry<String, PrivatePayment> entry : payment.getPrivatePayment().entrySet()) {
                    PrivatePayment privatePayment = entry.getValue();

                    // 제거할 사람 반영
                    if (removedMembers.contains(entry.getKey())) {
                        privatePayment.getPublicPaymentList().remove(payId);
                    }

                    // 추가할 사람 반영
                    if (addedMembers.contains(entry.getKey())) {
                        privatePayment.getPublicPaymentList().add(payId);
                    }
                }

                // `addedMembers`에 포함된 사용자 중 `privatePayment` 맵에 없는 사용자를 추가
                for (String memberId : addedMembers) {
                    if (!payment.getPrivatePayment().containsKey(memberId)) {
                        PrivatePayment newPrivatePayment = new PrivatePayment();
                        newPrivatePayment.setPublicPaymentList(new ArrayList<>());
                        newPrivatePayment.setPrivatePaymentList(new ArrayList<>());
                        newPrivatePayment.getPublicPaymentList().add(payId);
                        payment.getPrivatePayment().put(memberId, newPrivatePayment);
                    }
                }

                //payId에 해당하는 공동 지출 내역 변경 및 반영
                payment.getPublicPayment().replace(payId, updatePublicPayment);
            }
            // 공동 지출에서 개인 지출로 변경
            else {
                // 개인 지출 생성
                makePrivatePaymentInfo(paymentInfo, userId, payment);

                // 공동 지출에서 해당 지출을 제거
                deletePublicPayment(payId, payment);
            }
        }
        // 개인 지출 내용 변경
        else {
            // 개인 지출 찾기
            Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
            List<PrivatePaymentInfo> privatePaymentList = privatePayment.get(userId).getPrivatePaymentList();

            int listIdx = 0;
            for (PrivatePaymentInfo currPrivatePaymentInfo : privatePaymentList) {
                if (currPrivatePaymentInfo.getPrivatePaymentId().equals(payId)) {
                    privateFlag = true;
                    break;
                }
                listIdx++;
            }
            // 개인 지출에서 공동 지출로 변경
            if (paymentInfo.getPayType().equals("public")) {
                // 개인 지출에서 제거
                if (privateFlag) {
                    PrivatePaymentInfo removePayment = privatePaymentList.remove(listIdx);
                    log.debug(removePayment.toString());
                }

                // 공동 지출 생성
                makePublicPayment(paymentInfo, payment);
            }
            else {
                // 개인 지출 항목 생성
                PrivatePaymentInfo updatePrivatePaymentInfo = makePrivatePayment(paymentInfo);

                // 있는 경우
                if (privateFlag) {
                    privatePaymentList.set(listIdx, updatePrivatePaymentInfo);
                }
            }
        }

        // 둘다 없는 경우 에러 발생
        if(!publicFlag && !privateFlag) {
            throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
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
                deletePublicPayment(payId, payment);
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

//        // tourId로 기존 데이터 가져오기
//        PaymentInfo payment = paymentInfoRepository.findById(tourId)
//                .orElseThrow(() -> new PaymentNotExistException("해당하는 지출정보가 없습니다."));

        // tourId로 기존 데이터 가져오기, 없으면 생성
        PaymentInfo payment = paymentInfoRepository.findById(tourId)
                .orElseGet(() -> PaymentInfo.builder()
                        .id(tourId)
                        .publicPayment(new LinkedHashMap<>())
                        .privatePayment(new LinkedHashMap<>())
                        .build());

        if(payment.getPublicPayment().isEmpty() && payment.getPrivatePayment().isEmpty())
            return new ArrayList<>();

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
            log.debug(paymentInfoRes.toString());
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
                        return makePaymentInfoRes(tourId, userId, currPrivatePaymentInfo);
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

        // publicPayment 의 ghostId 변경
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

        // private 의 ghostId 변경
        privatePaymentMap.put(userId, privatePayment);
        privatePaymentMap.remove(ghostId);

        // 디비에 저장
        paymentInfoRepository.save(payment);
    }

    // 공통 지출 만들기
    private String makePublicPayment(PaymentInfoReq paymentInfo, PaymentInfo payment) {
        String publicPaymentId = UUID.randomUUID().toString();
        PublicPayment publicPayment = makePublicPayment(paymentInfo);

        // 반영
        Map<String, PublicPayment> publicPaymentMap = payment.getPublicPayment();
        publicPaymentMap.put(publicPaymentId, publicPayment);
        payment.setPublicPayment(publicPaymentMap);

        // 공동 구매 내용 반영
        List<PaymentMember> payMemberList = paymentInfo.getPayMemberList();
        for(PaymentMember payMember : payMemberList) {
            if (!payment.getPrivatePayment().containsKey(payMember.getUserId())) {
                // 개인 지출 항목 생성
                ArrayList<String> publicPaymentList = new ArrayList<>();
                publicPaymentList.add(publicPaymentId);

                PrivatePayment updatePrivatePayment = PrivatePayment.builder()
                        .privatePaymentList(new ArrayList<>())
                        .publicPaymentList(publicPaymentList)
                        .build();
                // 반영
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                privatePayment.put(payMember.getUserId(), updatePrivatePayment);
                payment.setPrivatePayment(privatePayment);
            } else {
                // 기존 항목에 추가
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                List<String> publicPaymentList = privatePayment.get(payMember.getUserId()).getPublicPaymentList();
                publicPaymentList.add(publicPaymentId);
                payment.setPrivatePayment(privatePayment);
            }
        }

        boolean payerIdExists = payMemberList.stream()
                .anyMatch(member -> member.getUserId().equals(paymentInfo.getPayerId()));

        if (!payerIdExists) {
            // Payer's payAmount is 0 as per the requirement that payer info shouldn't have payAmount
            if (!payment.getPrivatePayment().containsKey(paymentInfo.getPayerId())) {
                // 개인 지출 항목 생성
                ArrayList<String> publicPaymentList = new ArrayList<>();
                publicPaymentList.add(publicPaymentId);

                PrivatePayment updatePrivatePayment = PrivatePayment.builder()
                        .privatePaymentList(new ArrayList<>())
                        .publicPaymentList(publicPaymentList)
                        .build();
                // 반영
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                privatePayment.put(paymentInfo.getPayerId(), updatePrivatePayment);
                payment.setPrivatePayment(privatePayment);
            } else {
                // 기존 항목에 추가
                Map<String, PrivatePayment> privatePayment = payment.getPrivatePayment();
                List<String> publicPaymentList = privatePayment.get(paymentInfo.getPayerId()).getPublicPaymentList();
                publicPaymentList.add(publicPaymentId);
                payment.setPrivatePayment(privatePayment);
            }
        }

        return publicPaymentId;
    }

    // 개인 지출 만들기
    private PrivatePaymentInfo makePrivatePaymentInfo(PaymentInfoReq paymentInfo, String userId, PaymentInfo payment) {
        PrivatePaymentInfo privatePaymentInfo = makePrivatePayment(paymentInfo);

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
        return privatePaymentInfo;
    }

    // 공동 지출 제거
    private void deletePublicPayment(String payId, PaymentInfo payment) {
        if (payment.getPublicPayment().containsKey(payId)) {
            // payMember 리스트에 해당하는 private 반영된 정보 제거
            List<String> payMemberList = payment.getPublicPayment().get(payId).getPayMemberList().stream()
                    .map(PaymentMember::getUserId)
                    .toList();

            for (String selectedUserId : payMemberList) {
                PrivatePayment privatePayment = payment.getPrivatePayment().get(selectedUserId);
                if (privatePayment != null) {
                    privatePayment.getPublicPaymentList().remove(payId);
                }
            }

            // payerId에 해당하는 곳에서도 제거
            List<String> publicPaymentList = payment.getPrivatePayment()
                    .get(payment.getPublicPayment().get(payId).getPayerId())
                    .getPublicPaymentList();

            for (String selectedPayId : publicPaymentList) {
                if(selectedPayId.equals(payId)) {
                    publicPaymentList.remove(payId);
                    break;
                }
            }

            // 해당하는 지출 제거
            PublicPayment removedPayment = payment.getPublicPayment().remove(payId);
            log.debug(removedPayment.toString());


        } else {
            throw new PaymentNotExistException("해당하는 지출정보가 없습니다.");
        }
    }

    private PublicPayment makePublicPayment(PaymentInfoReq paymentInfo) {
        return PublicPayment.builder()
                .payAmount(paymentInfo.getPayAmount())
                .exchangeRate(paymentInfo.getExchangeRate())
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

    private PrivatePaymentInfo makePrivatePayment(PaymentInfoReq paymentInfo) {
        return PrivatePaymentInfo.builder()
                .privatePaymentId(UUID.randomUUID().toString())
                .payAmount(paymentInfo.getPayAmount())
                .exchangeRate(paymentInfo.getExchangeRate())
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
                .exchangeRate(publicPayment.getExchangeRate())
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
                .exchangeRate(currPrivatePayment.getExchangeRate())
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
