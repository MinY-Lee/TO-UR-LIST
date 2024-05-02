package com.eminyidle.payment.batch;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ExchangeRateTasklet implements Tasklet {

    @Value("${EXCHANGERATE_KEY}")
    String authKey;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);

        // 요청 매개변수 설정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        String url = "https://v6.exchangerate-api.com/v6/" + authKey + "/latest/KRW";
        // 환율 요청
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 응답 처리
        String responseBody = response.getBody();

        log.info("Response Body: {}", responseBody);
        // JSON 파싱
        JSONObject jsonObject = new JSONObject(responseBody);

        return RepeatStatus.FINISHED;
    }
}
