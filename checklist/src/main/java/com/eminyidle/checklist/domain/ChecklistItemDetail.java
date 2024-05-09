package com.eminyidle.checklist.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistItemDetail extends ChecklistItem{
    private Boolean isPublic;

    public ChecklistItemDetail(String tourId, String placeId, String tourActivityId, Integer tourDay, String item, Boolean isChecked, Boolean isPublic) {
        super(tourId, placeId, tourActivityId, tourDay, item, isChecked);
        setIsPublic(isPublic);
    }
}
