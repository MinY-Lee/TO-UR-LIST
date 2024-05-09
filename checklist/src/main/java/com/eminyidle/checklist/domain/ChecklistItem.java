package com.eminyidle.checklist.domain;

import com.eminyidle.checklist.exception.InvalidItemNameException;
import com.eminyidle.checklist.exception.InvalidTourDayException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChecklistItem {
    @Setter
    private String tourId;
    @Setter
    private String placeId;
    @Setter
    private String tourActivityId; //TODO- 바꿔야 함
    private Integer tourDay;
    private String item;
    @Setter
    private Boolean isChecked;

    public ChecklistItem(String tourId, String placeId, String tourActivityId, Integer tourDay, String item, Boolean isChecked) {
        setTourId(tourId);
        setPlaceId(placeId);
        setTourActivityId(tourActivityId);
        setTourDay(tourDay);
        setItem(item);
        setIsChecked(isChecked);
    }

    public void setTourDay(Integer tourDay){
        if(tourDay<0) throw new InvalidTourDayException();
        this.tourDay=tourDay;
    }

    public void setItem(String item){
        if (item==null) throw new InvalidItemNameException();
        item=item.strip();
        if (item.length()<1) throw new InvalidItemNameException();
        this.item=item;
    }
}
