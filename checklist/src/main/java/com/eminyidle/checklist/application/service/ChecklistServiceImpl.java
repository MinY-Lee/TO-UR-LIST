package com.eminyidle.checklist.application.service;

import com.eminyidle.checklist.application.port.ChecklistService;
import com.eminyidle.checklist.application.port.in.ChangeTourActivityUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourMemberUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourPlaceUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourUsecase;
import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.dto.Take;
import com.eminyidle.checklist.dto.Tour;
import com.eminyidle.checklist.dto.User;
import com.eminyidle.checklist.exception.*;
import com.eminyidle.checklist.adapter.out.persistence.ItemRepository;
import com.eminyidle.checklist.adapter.out.persistence.TakeRepository;
import com.eminyidle.checklist.adapter.out.persistence.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
//@Transactional
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService, ChangeTourUsecase, ChangeTourMemberUsecase, ChangeTourPlaceUsecase, ChangeTourActivityUsecase {

    private final TourRepository tourRepository;
    private final ItemRepository itemRepository;
    private final TakeRepository takeRepository;

    private Tour assertUserInTour(String userId, String tourId) {
        return tourRepository.findTourByUserIdAndTourId(userId, tourId).orElseThrow(UserNotInTourException::new);
    }

    @Override
    public boolean createPrivateItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
        return createItem(userId, checklistItem, false);
    }
    @Override
    public boolean createPublicItem(String userId, ChecklistItem checklistItem) {
        Tour tour = assertUserInTour(userId, checklistItem.getTourId());
        log.debug(tour.toString());
        //이미 공동물품으로 등록되어 있는 경우
        if (takeRepository.existsPublicRelationship(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem())) {
            //나도 가져가는 물품으로 되어 있는 경우
            if (takeRepository.existsTakeRelationshipByUserId(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem())) {
                return true;
            }
            //나에게는 삭제되어있던 경우
            createItem(userId, checklistItem, true);
            return false;
        }
        itemRepository.createPublicRelation(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem());
        for (User member : tour.getMemberList()) {
            log.debug(">>" + member.getUserId());
            createItem(member.getUserId(), checklistItem, true);
        }

        return false;
    }

    private boolean createItem(String userId, ChecklistItem item, boolean isPublic, boolean isChecked, LocalDateTime createdAt) {
        //내가 기존에 추가해둔 아이템인지 확인: 중복되는 경우, create를 수행하지 않는다
        if (takeRepository.existsTakeRelationshipByUserId(userId, item.getTourId(), item.getPlaceId(), item.getTourDay(), item.getActivity(), item.getItem())) {
            return true;
        }

        String itemType;
        if (isPublic) itemType = "public";
        else itemType = "private";

        itemRepository.save(userId, item.getTourId(), item.getPlaceId(), item.getTourDay(), item.getActivity(), item.getItem(), itemType, createdAt, isChecked).orElseThrow(CreateItemException::new);
        return false;
    }

    private boolean createItem(String userId, ChecklistItem item, boolean isPublic) {
        return createItem(userId, item, isPublic, false, LocalDateTime.now());
    }

    @Override
    public boolean updateItem(String userId, ChecklistItem oldItem, ChecklistItem newItem) {
        assertUserInTour(userId, oldItem.getTourId());
        // 수정 전 아이템 존재 확인 -> 없으면 에러
        Take takenItem = takeRepository.findTakeRelationshipByUserId(userId, oldItem.getTourId(), oldItem.getPlaceId(), oldItem.getTourDay(), oldItem.getActivity(), oldItem.getItem()).orElseThrow(NoSuchItemException::new);
        log.debug(takenItem.toString());

        if(createItem(userId, newItem, false, takenItem.getIsChecked(), takenItem.getCreatedAt())){
            return true;
        }
        assertedDeleteItem(userId,oldItem);
        return false;
    }

    @Override
    public void deleteItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
        assertedDeleteItem(userId, checklistItem);
    }
    //이미 asserted된 내용에 대해 삭제
    private void assertedDeleteItem(String userId, ChecklistItem checklistItem){
        Take takenItem = takeRepository.findTakeRelationshipByUserId(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem()).orElseThrow(NoSuchItemException::new);
        log.debug("ITEMTYPE: " + takenItem.toString());
        itemRepository.deletePrivateItemRelationship(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem());

        //PRIVATE-public이 없다면 PUBLIC도 없애는 로직
        if ("public".equals(takenItem.getType())) {
            List<Take> leftPublicTakeList = takeRepository.findTakePublicRelationshipList(checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem());
            if (leftPublicTakeList.isEmpty()) {
                itemRepository.deletePublicRelationship(checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem());
            }
        }
    }

    @Override
    public List<ChecklistItemDetail> searchItemList(String userId, String tourId) {
        assertUserInTour(userId, tourId);
        return itemRepository.findChecklistItemDetailByUserIdAndTourId(userId, tourId);
    }

    @Override
    public void checkItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
        itemRepository.updateItemIsChecked(userId, checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem(), checklistItem.getIsChecked());
    }


    @Override
    public void createTour() {
        // tour 생성
        //updateCountry()
        // 장소 없음 tour_place 생성
        // 활동 없음 tour_activity 생성
    }

    @Override
    public void deleteTour() {
        // tour와 관련된 모든 tour_activity 삭제
        // 모든 tour_place 삭제
        // tour 삭제
    }

    @Override
    public void updateCountry() {
        //이전 country로 연결되어있는 모든 관계 끊고
        //새로운 country로 연결된 모든 관계 연결
        // common은 냅두고
        // 다른 것들만... 끊고 추가한다
    }

    @Override
    public void createMember() {
        //public 연결이 있는 모든 것들 찾아서.. private 그 유저아이디 연결
    }

    @Override
    public void deleteMember() {
        //모든 private 그 userId 삭제
    }

    @Override
    public void createPlace() {
        // 맞는 tour 확인 -> 없음 에러
        // tour_place 생성
        // 그에 맞게 활동 없음 tour_activity 생성
    }

    @Override
    public void deletePlace() {
        // tour-place와 관련된 모든 tour_activity 삭제
        // tour_place 삭제
    }

    @Override
    public void updatePlace() { //장소 날짜 수정
        //TODO: 잘 합쳐 줘야 함..
        //해당 날짜에 있는 활동과, 그 장소가 가진 활동들 잘 합쳐 줘야 함
    }

    @Override
    public void createActivity() {
        // tour의 tour_place 확인
        // tour_activity 생성
    }

    @Override
    public void deleteActivity() {
        // tour_activity 삭제
    }

}
