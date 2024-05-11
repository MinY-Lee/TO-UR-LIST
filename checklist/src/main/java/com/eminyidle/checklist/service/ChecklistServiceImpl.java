package com.eminyidle.checklist.service;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.dto.Tour;
import com.eminyidle.checklist.dto.User;
import com.eminyidle.checklist.exception.CreateItemException;
import com.eminyidle.checklist.exception.UserNotInTourException;
import com.eminyidle.checklist.repository.ItemRepository;
import com.eminyidle.checklist.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService{

    private final TourRepository tourRepository;
    private final ItemRepository itemRepository;

    private Tour assertUserInTour(String userId, String tourId){
        return tourRepository.findTourByUserIdAndTourId(userId,tourId).orElseThrow(UserNotInTourException::new);
    }
    @Override
    public void createPrivateItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
        createItem(userId,checklistItem,false);
    }

    private void createItem(String userId, ChecklistItem item, boolean isPublic){
        String itemType="private";
        if(isPublic) itemType="public";

        itemRepository.save(userId,item.getTourId(),item.getPlaceId(),item.getTourDay(),item.getActivity(),item.getItem(), itemType, LocalDateTime.now()).orElseThrow(CreateItemException::new);
    }

    @Override
    public void createPublicItem(String userId, ChecklistItem checklistItem) {
        Tour tour=assertUserInTour(userId, checklistItem.getTourId());
//        tour.setMemberList(tourRepository.findMemberByTourId(checklistItem.getTourId()));
        log.debug(tour.toString());
        for(User member: tour.getMemberList()){
            log.debug(">>"+member.getUserId());
            createItem(member.getUserId(),checklistItem,true);
        }

    }

    @Override
    public void updateItem(String userId, ChecklistItem oldItem, ChecklistItem newItem) {
        assertUserInTour(userId, oldItem.getTourId());

    }

    @Override
    public void deleteItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
    }

    @Override
    public List<ChecklistItemDetail> searchItemList(String userId, String tourId) {
        assertUserInTour(userId,tourId);
        return itemRepository.findChecklistItemDetailByUserIdAndTourId(userId,tourId);
    }

    @Override
    public void checkItem(String userId, ChecklistItem checklistItem) {
        assertUserInTour(userId, checklistItem.getTourId());
    }
}
