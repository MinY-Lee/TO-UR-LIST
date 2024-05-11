package com.eminyidle.checklist.domain;

import com.eminyidle.checklist.exception.InvalidItemNameException;
import com.eminyidle.checklist.exception.InvalidTourDayException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ChecklistItem {
    @Setter
    private String tourId;
    @Setter
    private String placeId;
    @Setter
    private String activity;
    private Integer tourDay;
    private String item;
    @Setter
    private Boolean isChecked;

    public ChecklistItem(String tourId, String placeId, String activity, Integer tourDay, String item, Boolean isChecked) {
        setTourId(tourId);
        setPlaceId(placeId);
        setActivity(activity);
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
