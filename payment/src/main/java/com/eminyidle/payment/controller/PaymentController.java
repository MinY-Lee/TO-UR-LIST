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

    @GetMapping("/currency/{countryCode}/{date}")
    public ResponseEntity<?> getCountryCurrencyRate(@Valid @NotBlank @PathVariable("countryCode") String countryCode,
                                                    @Valid @NotBlank @PathVariable("date") String date) {

        log.info(countryCode);
        log.info(date);
        //날짜 파싱
        String parseDate = date.replace("-", "");

        return ResponseEntity.ok().body("good");
    }
}
