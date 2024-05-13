package com.eminyidle.checklist.application.port;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;

import java.util.List;

public interface ChecklistService {

    boolean createPrivateItem(String userId, ChecklistItem checklistItem);

    boolean createPublicItem(String userId, ChecklistItem checklistItem);

    boolean updateItem(String userId, ChecklistItem oldItem, ChecklistItem newItem);

    void deleteItem(String userId, ChecklistItem checklistItem);

    List<ChecklistItemDetail> searchItemList(String userId, String tourId);

    //체크 및 체크 해제
    void checkItem(String userId, ChecklistItem checklistItem);
}
