package com.eminyidle.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
@RequestMapping("/pay")
public class PaymentController {

//    @Value("")
    String authKey ="o4HdBtlosRAOh1X0o3MTqrrTtZ1OSKVt";

    @GetMapping("/currency/{countryCode}/{date}")
    public ResponseEntity<?> getCountryCurrencyRate(@Valid @NotBlank @PathVariable("countryCode") String countryCode,
                                                    @Valid @NotBlank @PathVariable("date") String date) {

        log.info(countryCode);
        log.info(date);
        //날짜 파싱
        String parseDate = date.replace("-", "");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);

        // 요청 매개변수 설정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + authKey
                + "&searchdate=" + parseDate + "&data=AP01";
        // 환율 요청
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 응답 처리
        String responseBody = response.getBody();

        // JSON 파싱
//        JSONObject jsonObject = new JSONObject(responseBody);
        System.out.println("Response Body: " + responseBody);
        // 액세스 토큰 추출
//        String unit = jsonObject.getString("CUR_UNIT");
        log.info("data : {}", responseBody);
        return ResponseEntity.ok().body("good");
    }
}
