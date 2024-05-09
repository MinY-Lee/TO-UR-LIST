package com.eminyidle.checklist.service;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;

import java.util.List;

public interface ChecklistService {

    void createPrivateItem(String userId, ChecklistItem checklistItem);

    void createPublicItem(String userId, ChecklistItem checklistItem);

    void updateItem(String userId, ChecklistItem oldItem, ChecklistItem newItem);

    void deleteItem(String userId, ChecklistItem checklistItem);

    List<ChecklistItemDetail> searchItemList(String userId, String tourId);

    //체크 및 체크 해제
    void checkItem(String userId, ChecklistItem checklistItem);
}
