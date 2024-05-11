package com.eminyidle.checklist.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChecklistServiceTest {

    @Autowired
    private ChecklistService checklistService;

    @Test
    @DisplayName("개인 항목 추가 - 그냥 여행에만 추가")
    void createPrivateItem1() {
    }
    @Test
    @DisplayName("개인 항목 추가 - 특정 장소에 추가")
    void createPrivateItem2() {
    }
    @Test
    @DisplayName("개인 항목 추가 - 특정 활동에 추가")
    void createPrivateItem3() {
        //다른 멤버는 확인할 수 없어야 함
    }

    @Test
    @DisplayName("개인 항목 추가 오류- 없는 여행에 추가")
    void createPrivateItemErr1() {
    }

    @Test
    @DisplayName("개인 항목 추가 오류- 없는 장소에 추가")
    void createPrivateItemErr2() {
    }

    @Test
    @DisplayName("개인 항목 추가 오류- 없는 활동에 추가")
    void createPrivateItemErr3() {
    }

    @Test
    @DisplayName("개인 항목 추가- 아이템 이름 중복")
    void createPrivateItem() {
        //isDuplicated=true 확인
    }

    @Test
    @DisplayName("공동 항목 추가-새로운 아이템")
    void createPublicItem() {
        //모든 멤버에게 다 추가되는지 확인
    }

    @Test
    @DisplayName("공동 항목 추가- 본인의 개인 아이템과 이름 중복")
    void createPublicItem1() {
        //모든 멤버에게 다 추가되는지 확인
    }

    @Test
    @DisplayName("공동 항목 추가- 기존에 이미 등록된 공동 아이템 생성")
    void createPublicItem2() {
        //공동 항목 타입인지 확인
        //삭제했던 멤버들에게 다시 생성
    }

    @Test
    @DisplayName("공동 항목 추가- 누군가의 개인 아이템으로 등록된 공동 아이템 생성")
    void createPublicItem3() {
        //공동 항목 타입인지 확인
        //모든 멤버들에게 생성 확인
    }
    @Test
    @DisplayName("공동 항목 추가- 중복된 공동 아이템 생성")
    void createPublicItem4() {
        //나의 공동 항목으로 되어있는데, 그걸 또 추가하는 경우
        //isDuplcated=true
    }
    @Test
    @DisplayName("공동 항목 추가 오류- 등록되지 않은 활동에 추가")
    void createPublicItemErr1() {
        //추가되면 안 됨
    }

    @Test
    @DisplayName("공동 항목 추가 오류- 잘못된 여행에 추가")
    void createPublicItemErr2() {
        //추가되면 안 됨
    }
    @Test
    @DisplayName("공동 항목 추가 오류- 추방된 사용자가 추가")
    void createPublicItemErr3() {
        //추가되면 안 됨
    }

    @Test
    @DisplayName("항목 수정-내용 수정")
    void updateItem1() {
        //기존 내용은 없어야 하고
        //바꾼 내용은 있어야 함
    }
    @Test
    @DisplayName("항목 수정-내용 수정 사항이 기존 내용과 겹침")
    void updateItem1_1() {
        //수정사항 반영 안됨
        //isDuplicated=true
    }

    @Test
    @DisplayName("항목 수정-다른 활동으로 수정")
    void updateItem2() {
        //기존 내용은 없어야 하고
        //바꾼 내용은 있어야 함
    }

    @Test
    @DisplayName("항목 수정 오류-없는 활동으로 수정")
    void updateItemErr2() {
        //기존 내용은 없어야 하고
        //바꾼 내용은 있어야 함
    }

    @Test
    @DisplayName("항목 수정-다른 장소로 수정")
    void updateItem3() {
        //기존 내용은 없어야 하고
        //바꾼 내용은 있어야 함
    }

    @Test
    @DisplayName("항목 수정 오류- 없는 장소로 수정")
    void updateItemErr3() {
        //기존 내용은 없어야 하고
        //바꾼 내용은 있어야 함
    }


    @Test
    @DisplayName("항목 수정 - 체크 여부 수정")
    void updateItem4() {
        //수정사항이 반영되면 안 됨(체크 여부는 다른 서비스 이용)
    }

    @Test
    @DisplayName("항목삭제-개인아이템")
    void deleteItem() {
        //다른 멤버의 아이템에는 영향 주면 안됨
        //내 연결만 끊기
    }
    @Test
    @DisplayName("항목삭제 오류-없는 개인아이템")
    void deleteItemErr() {
        //다른 멤버의 아이템에는 영향 주면 안됨
        //반영 안됨
    }
    @Test
    @DisplayName("항목삭제-모두에게 사라진 공동아이템")
    void deleteItem1() {
        //내 연결만 끊기
        //모두에게 삭제되었다면 공동아이템 연결 끊어짐
    }

    @Test
    @DisplayName("항목삭제-공동아이템")
    void deleteItem2() {
        //다른 멤버의 아이템에는 영향 주면 안됨
        //내 연결만 끊기
    }
    @Test
    @DisplayName("항목삭제 오류-없는 공동아이템")
    void deleteItemErr2() {
        //다른 멤버의 아이템에는 영향 주면 안됨
        //반영 안됨
    }

    @Test
    @DisplayName("리스트 조회")
    void searchItemList() {
    }
    @Test
    @DisplayName("리스트 조회 오류-투어에 포함된 유저 아님")
    void searchItemListErr() {
    }

    @Test
    @DisplayName("항목 체크")
    void checkItem() {
        //true->true
        //false->false
        //true->false
        //false->true
    }
    @Test
    @DisplayName("항목 체크 오류-대상 항목 없음")
    void checkItemErr() {
    }

    //// checklist 자동 생성 관련
    @Test
    @DisplayName("나라 변경")
    void updateItemByCountry() {
        //새로 추가된 아이템 생기는지
        //기존에 체크해둔 아이템은 삭제 안됨
        //체크 안해둔 아이템은 사라짐
    }
    @Test
    @DisplayName("나라 변경-추가")
    void updateItemByCountry1() {
        //추가적으로 새로 추가된 아이템 생기는지
    }
    @Test
    @DisplayName("나라 변경-삭제")
    void updateItemByCountry2() {
        //기존에 체크해둔 아이템은 삭제 안됨
        //체크 안해둔 아이템은 삭제됨
    }

    @Test
    @DisplayName("활동 변경-추가")
    void updateItemByActivity1() {
        //새로 추가된 아이템 생기는지
    }

    @Test
    @DisplayName("활동 변경-삭제")
    void updateItemByActivity2() {
        //기존에 체크해둔 아이템은 삭제 안됨 -> 관련 활동 기본(활동없음) 으로 변경됨
    }

    @Test
    @DisplayName("장소 변경-삭제")
    void updateItemByPlace() {
        //기존에 체크해둔 아이템은 삭제 안됨 -> 관련 활동 기본(장소없음-활동없음) 으로 변경됨
    }
}