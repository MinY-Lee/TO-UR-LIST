//package com.eminyidle.payment.batch;
//
//
//import com.eminyidle.payment.config.ExchangeRateJobConfig;
//import com.eminyidle.payment.config.TestBatchConfig;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//
//@SpringBootTest
//@SpringBatchTest
//@SpringJUnitConfig(classes = {ExchangeRateJobConfig.class, TestBatchConfig.class})
//class ExchangeRateJobTest {
//
//    @Autowired
//    JobLauncherTestUtils jobLauncherTestUtils;
//
//    @Autowired
//    ExchangeRateTasklet exchangeRateTasklet;
//
//    @Test
//    public void 배치테스트() throws Exception {
//        // 외부 api에서 받아온 정보이므로 Mock을 써서 확인
////            batchService.saveExchangeRates(Mock);
//        Mockito.when(exchangeRateTasklet.execute(Mockito.any(StepContribution.class),Mockito.any(ChunkContext.class))).thenReturn(RepeatStatus.FINISHED);
//
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
//    }
//}