package com.eminyidle.checklist.service;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistServiceImpl implements ChecklistService{
    @Override
    public void createPrivateItem(String userId, ChecklistItem checklistItem) {

    }

    @Override
    public void createPublicItem(String userId, ChecklistItem checklistItem) {

    }

    @Override
    public void updateItem(String userId, ChecklistItem oldItem, ChecklistItem newItem) {

    }

    @Override
    public void deleteItem(String userId, ChecklistItem checklistItem) {

    }

    @Override
    public List<ChecklistItemDetail> searchItemList(String userId, String tourId) {
        return null;
    }

    @Override
    public void checkItem(String userId, ChecklistItem checklistItem) {

    }
}
