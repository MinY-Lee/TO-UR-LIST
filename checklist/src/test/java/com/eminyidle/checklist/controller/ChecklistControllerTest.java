package com.eminyidle.checklist.controller;

import com.eminyidle.checklist.service.ChecklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.AutoConfigureDataNeo4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest
class ChecklistControllerTest {

    @Autowired
    private ChecklistController checklistController;
    @MockBean
    private ChecklistService checklistService;

    @Test
    @DisplayName("정상:체크리스트 조회")
    void searchChecklist() {

    }

    @Test
    @DisplayName("정상:체크리스트 개인항목 추가")
    void createPrivateItem() {
    }

    @Test
    @DisplayName("정상:체크리스트 공동항목 추가")
    void createPublicItem() {
    }

    @Test
    @DisplayName("정상:체크리스트 항목 업데이트")
    void updateItem() {
    }

    @Test
    @DisplayName("정상:체크리스트 항목 삭제")
    void deleteItem() {
    }

    @Test
    @DisplayName("정상:체크리스트 체크")
    void checkItem() {
    }

    //Err
    @Test
    @DisplayName("err:체크리스트 조회-유저 아이디 없음")
    void searchChecklistErr() {

    }

    @Test
    @DisplayName("err:체크리스트 개인항목 추가-유저 아이디 없음")
    void createPrivateItemErr() {
    }

    @Test
    @DisplayName("err:체크리스트 공동항목 추가-유저 아이디 없음")
    void createPublicItemErr() {
    }

    @Test
    @DisplayName("err:체크리스트 항목 업데이트-유저 아이디 없음")
    void updateItemErr() {
    }

    @Test
    @DisplayName("err:체크리스트 항목 삭제-유저 아이디 없음")
    void deleteItemErr() {
    }

    @Test
    @DisplayName("err:체크리스트 체크-유저 아이디 없음")
    void checkItemErr() {
    }

    @Test
    @DisplayName("err:체크리스트 조회-잘못된 tourId")
    void searchChecklistErr1() {
        //tourId없음
        //이상한 tourId
        //참여중이지 않은 tourId
    }

    @Test
    @DisplayName("err:체크리스트 개인항목 추가-잘못된 requestBody")
    void createPrivateItemErr1() {
        //placeId없음: 에러
        //tourDay없음: 에러
        //아이템 없음: 에러
        //이상한 값 추가적으로 있음(tourTitle) -> 로직상은 상관없으나, 에러 뜨나?
        //isChecked 값 없음: 상관 없음
    }

    @Test
    @DisplayName("err:체크리스트 공동항목 추가-잘못된 requestBody")
    void createPublicItemErr1() {
        //placeId없음: 에러
        //tourDay없음: 에러
        //아이템 없음: 에러
        //이상한 값 추가적으로 있음(tourTitle) -> 로직상은 상관없으나, 에러 뜨나?
        //isChecked 값 없음: 상관 없음
    }

    @Test
    @DisplayName("err:체크리스트 항목 업데이트-잘못된 requestBody")
    void updateItemErr1() {
        //oldItem/newItem 없음: 에러
        //각 아이템에..
            //placeId없음: 에러
            //tourDay없음: 에러
            //아이템 없음: 에러
            //이상한 값 추가적으로 있음(tourTitle) -> 로직상은 상관없으나, 에러 뜨나?
            //isChecked 값 없음: 상관 없음
    }

    @Test
    @DisplayName("err:체크리스트 항목 삭제-잘못된 requestBody")
    void deleteItemErr1() {
        //placeId없음: 에러
        //tourDay없음: 에러
        //아이템 없음: 에러
        //이상한 값 추가적으로 있음(tourTitle) -> 로직상은 상관없으나, 에러 뜨나?
        //isChecked 값 없음: 상관 없음
    }

    @Test
    @DisplayName("err:체크리스트 체크-잘못된 requestBody")
    void checkItemErr1() {
        //placeId없음: 에러
        //tourDay없음: 에러
        //아이템 없음: 에러
        //이상한 값 추가적으로 있음(tourTitle) -> 로직상은 상관없으나, 에러 뜨나?
        //isChecked 값 없음: 에러
    }

}