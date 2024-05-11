package com.eminyidle.checklist.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataNeo4jTest
class tourPlaceRepositoryTest {

    @Autowired
    private tourPlaceRepository tourPlaceRepository;

    @Test
    public void existRepository(){
        assertThat(tourPlaceRepository).isNotNull();
    }
    @Test
    public void saveRepository(){
        assertThat(tourPlaceRepository).isNotNull();
    }

}