package com.eminyidle.checklist.application.service;

import com.eminyidle.checklist.adapter.out.persistence.*;
import com.eminyidle.checklist.application.dto.*;
import com.eminyidle.checklist.application.port.ChecklistService;
import com.eminyidle.checklist.application.port.in.ChangeTourActivityUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourMemberUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourPlaceUsecase;
import com.eminyidle.checklist.application.port.in.ChangeTourUsecase;
import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Transactional("neo4jTransactionManager")
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService, ChangeTourUsecase, ChangeTourMemberUsecase, ChangeTourPlaceUsecase, ChangeTourActivityUsecase {

    private final TourRepository tourRepository;
    private final TourPlaceRepository tourPlaceRepository;
    private final TourActivityRepository tourActivityRepository;
    private final ActivityRepository activityRepository;
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
        itemRepository.createPublicRelationship(checklistItem.getTourId(), checklistItem.getPlaceId(), checklistItem.getTourDay(), checklistItem.getActivity(), checklistItem.getItem());
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

        if (createItem(userId, newItem, false, takenItem.getIsChecked(), takenItem.getCreatedAt())) {
            return true;
        }
        assertedDeleteItem(userId, oldItem);
        return false;
    }

    @Override
    public void deleteItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
        assertedDeleteItem(userId, checklistItem);
    }

    //이미 asserted된 내용에 대해 삭제
    private void assertedDeleteItem(String userId, ChecklistItem checklistItem) {
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
    public void createTour(String tourId, Long tourPeriod, Set<String> countryCodeSet, String userId) {
        // tour 생성

        Tour tour = tourRepository.save(Tour.builder()
                .tourId(tourId)
                .tourPeriod(tourPeriod)
                .build());

        log.debug("tour 생성 완료! " + tour.toString());
        // 장소 없음 tour_place 생성 + 활동 없음 tour_activity 생성
        createPlace(tourId, "_default_" + tourId, "", 0,"");
//        updateCountry(tourId, countryCodeSet);
        createCountry(tourId,countryCodeSet);
        createMember(tourId,userId);
    }

    private void createCountry(String tourId, Set<String> countryCodeSet) {
        //나라 연결
        for(String countryCode: countryCodeSet){
            tourRepository.createToRelationshipBetweenTourAndCountry(tourId,countryCode);
            //특정 countryCode 연결
            for (Item item : itemRepository.findAllInCountryByCountryCode(countryCode)) {
                itemRepository.createPublicRelationship(tourId, "", 0, "", item.getItem());
            }
        }

        //common 연결
        for (Item item : itemRepository.findAllInCommonCountry()) {
            itemRepository.createPublicRelationship(tourId, "", 0, "", item.getItem());
        }

        //이전 country로 연결되어있는 모든 관계 끊고
        //새로운 country로 연결된 모든 관계 연결
        // common은 냅두고
        // 다른 것들만... 끊고 추가한다
        //필요한 아이템 -> public 관계 연결
    }
    @Override
    public void deleteTour(String tourId) {
        // 모든 tour_place 삭제: tour와 관련된 모든 tour_activity 삭제
        for (TourPlace tourPlace : tourPlaceRepository.findAllByTourId(tourId)) {
            deletePlace(tourPlace.getTourPlaceId());
        }
        // tour 삭제
        tourRepository.deleteById(tourId);
    }

    @Override
    public void updateCountry(String tourId, Set<String> countryCodeSet) {
        //이전 country로 연결되어있는 모든 관계 끊고
        //새로운 country로 연결된 모든 관계 연결
        // common은 냅두고
        // 다른 것들만... 끊고 추가한다
        //필요한 아이템 -> public 관계 연결
    }

    @Override
    public void createMember(String tourId, String userId) {
        if(tourRepository.existsMemberRelationshipByTourIdAndUserId(tourId,userId)){
            log.debug("중복된 멤버다아아아ㅏ아아아아아");
            //TODO- Exception일지 return일지..
            return; //중복된 경우
        }
        log.debug("멤버 생성...");
        tourRepository.createMemberRelationshipByTourIdAndUserId(tourId,userId);

        log.debug("멤버 생성끗...");
        //public 연결이 있는 모든 것들 찾아서.. private 그 유저아이디 연결
        itemRepository.createTakePublicRelationshipByUserIdAndTourId(userId,tourId);
        log.debug("아이템 연결 완료");
    }

    @Override
    public void deleteMember(String tourId, String userId) {
        //모든 private 그 userId 삭제
        itemRepository.deleteAllTakeItemRelationshipBuUserIdAndTourId(userId,tourId);
    }

    @Override
    public void createPlace(String tourId, String tourPlaceId, String placeId, Integer tourDay, String placeName) {
        // 맞는 tour 확인 -> 없음 에러
        Tour tour = tourRepository.findByTourId(tourId).orElseThrow(NoSuchTourException::new);
        log.debug("PLACE: tour 있다");
        if (tourPlaceRepository.existsById(tourPlaceId)) {
            log.debug("PLACE: ~@~@");
            log.debug(tourPlaceRepository.findById(tourPlaceId).orElseThrow(NoSuchTourPlaceException::new).toString());
            throw new DuplicatedTourPlaceException();
        }
        log.debug("place 준비");
        tourPlaceRepository.save(TourPlace.builder()
                .tourPlaceId(tourPlaceId)
                .placeAndTour(Go.builder()
                        .placeId(placeId)
                        .placeName(placeName)
                        .tourDay(tourDay)
                        .tour(tour)
                        .build())
                .build());
        log.debug("place 생성 완료");
        // 그에 맞게 활동 없음 tour_activity 생성
        createActivityByAssertedTourPlaceId(tourPlaceId, "");
    }

    @Override
    public void deletePlace(String tourId, String placeId, Integer tourDay) {
        // tour-place와 관련된 모든 tour_activity 삭제
        tourActivityRepository.deleteAll(tourActivityRepository.findAllByTourPlace(tourId, placeId, tourDay));
        // tour_place 삭제
        tourPlaceRepository.delete(tourId, placeId, tourDay);
    }

    private void deletePlace(String tourPlaceId) {
        // tour-place와 관련된 모든 tour_activity 삭제
        tourActivityRepository.deleteAll(tourActivityRepository.findAllByTourPlaceId(tourPlaceId));
        // tour_place 삭제
        tourPlaceRepository.deleteById(tourPlaceId);
    }

    @Override
    public void updatePlace(String tourId, String tourPlaceId, String placeId, Integer tourDayAfter) { //장소 날짜 수정
        //TODO: 잘 합쳐 줘야 함..

        // 맞는 tour 확인 -> 없음 에러
        Tour tour = tourRepository.findById(tourId).orElseThrow(NoSuchTourException::new);
        log.debug("PLACE: tour 있다");
        if (!tourPlaceRepository.existsByTourIdAndPlaceIdAndTourDay(tourId,placeId,tourDayAfter)) {
            //기존에 없는 경우 그냥 :GO의 tourDay만 바꿔주면 됨
            tourPlaceRepository.updateTourDayOfTourPlace(tourPlaceId,tourDayAfter);
            return;
        }
        TourPlace targetTourPlace=tourPlaceRepository.findByTourIdAndPlaceIdAndTourDay(tourId,placeId,tourDayAfter).orElseThrow(NoSuchTourPlaceException::new);

        tourActivityRepository.findAllByTourPlaceId(tourPlaceId).forEach(tourActivity -> {
            if(tourActivityRepository.existsByTourPlaceIdAndActivity(targetTourPlace.getTourPlaceId(), tourActivity.getActivity())){
                //기존에 동일한 활동 있다
                //해당 날짜에 있는 활동과, 그 장소가 가진 활동들 잘 합쳐 줘야 함
                //현재 tourPlaceId에 연결된 모든 활동을.... 대상 쪽으로 머지...!
                itemRepository.copyPublicRelationshipByTourActivtyIdAndTargetTourPlaceId(tourActivity.getTourActivityId(),targetTourPlace.getTourPlaceId());
                itemRepository.copyTakeRelationshipByTourActivtyIdAndTargetTourPlaceId(tourActivity.getTourActivityId(),targetTourPlace.getTourPlaceId());
                tourActivityRepository.deleteById(tourActivity.getTourActivityId());
            } else{
                //자신을 그곳에
                tourActivityRepository.updateTourPlaceByTourActivtyIdAndTargetTourPlaceId(tourActivity.getTourActivityId(),targetTourPlace.getTourPlaceId());
            }
        });
        //다 옮기면 현재 투어플레이스 삭제
        tourPlaceRepository.deleteById(tourPlaceId);
    }
    @Override
    public void createActivity(String tourId, String PlaceId, Integer tourDay, String activityName){
        TourPlace tourPlace = tourPlaceRepository.findByTourIdAndPlaceIdAndTourDay(tourId, PlaceId, tourDay).orElseThrow(NoSuchTourPlaceException::new);
        createActivityByAssertedTourPlaceId(tourPlace.getTourPlaceId(),activityName);
    }

    @Override
    public void createActivity(String tourPlaceId, String activityName) {
        // tour의 tour_place 확인
        tourPlaceRepository.findById(tourPlaceId).orElseThrow(NoSuchTourPlaceException::new);
        createActivityByAssertedTourPlaceId(tourPlaceId,activityName);
    }
    private void createActivityByAssertedTourPlaceId(String tourPlaceId, String activityName){
        // activity 확인
        log.debug("%%%%%%%% "+activityName);
        Activity activity = activityRepository.findById(activityName).orElseThrow(NoSuchActivityException::new);
        log.debug("ACTIVITY: 활동 확인 "+activity.toString());
        // tour_activity 생성
        TourActivity tourActivity = tourActivityRepository.save(tourPlaceId, activityName, UUID.randomUUID().toString());
        log.debug("ACTIVITY: 생성 완료");
        //필요한 아이템 -> public 관계 연결
        activity.getItemList().forEach(
                item -> {
                    itemRepository.createPublicRelationshipByTourActivityId(tourActivity.getTourActivityId(), item.getItem());
                    log.debug(item.getItem()+" 완료!");
                }
        );
        //TODO - 연결 확인
        //모든 멤버에 대해 TAKE 관계도 만들기!
        itemRepository.createTakePublicRelationshipByTourActivityId(tourActivity.getTourActivityId());

    }


    @Override
    public void deleteActivity(String tourPlaceId, String activityName) {
        // tour_activity 삭제
        tourActivityRepository.delete(tourPlaceId, activityName);
    }

}
