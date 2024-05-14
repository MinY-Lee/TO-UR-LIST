package com.eminyidle.payment.config;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import com.eminyidle.payment.repository.PaymentInfoRepository;

public class CleanUpTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        MongoTemplate mongoTemplate = testContext.getApplicationContext()
                .getBean(MongoTemplate.class);

        // 데이터베이스를 삭제
        mongoTemplate.getDb().drop();

//        // 테스트 컨텍스트에서 리포지토리 가져오기
//        PaymentInfoRepository mongoRepository = testContext.getApplicationContext()
//                .getBean(PaymentInfoRepository.class);
//
//        // 데이터베이스에서 모든 데이터를 삭제
//        mongoRepository.deleteAll();
    }

    @Override
    public int getOrder() {
        // 리스너 실행 순서를 정의합니다. 숫자가 낮을수록 먼저 실행됩니다.
        return Integer.MAX_VALUE; // 가장 마지막에 실행되도록 설정
    }
}