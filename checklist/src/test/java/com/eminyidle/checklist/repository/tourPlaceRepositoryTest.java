package com.eminyidle.checklist.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class tourPlaceRepositoryTest {

    @Autowired
    private tourPlaceRepository tourPlaceRepository;

    @Test
    public void existRepository(){
        assertThat(tourPlaceRepository).isNotNull();
    }

}