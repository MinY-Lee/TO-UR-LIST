package com.eminyidle.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate kafkaTemplate;

    public void produceMessage(String topic, String value){
        log.debug("produceMessage-"+topic);
        CompletableFuture send = kafkaTemplate.send(topic, value);
    }
}
