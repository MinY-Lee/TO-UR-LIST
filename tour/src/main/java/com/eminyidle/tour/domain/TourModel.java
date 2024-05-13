package com.eminyidle.tour.domain;

import com.eminyidle.tour.application.dto.City;
import com.eminyidle.tour.exception.InvalidTourDateException;
import com.eminyidle.tour.exception.InvalidTourTitleFormatException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class TourModel {
    //제목, 기간, 도시
    @Setter
    String tourId;

    String tourTitle;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime startDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime endDate;

    @Setter
    List<TourCity> cityList;

    public TourModel(String tourId, String tourTitle, LocalDateTime startDate, LocalDateTime endDate, List<TourCity> cityList) {
        setTourId(tourId);
        setTourTitle(tourTitle);
        setStartDate(startDate);
        setEndDate(endDate);
        setCityList(cityList);
    }

    public void setTourTitle(String tourTitle){
        if(tourTitle==null) throw new InvalidTourTitleFormatException();
        tourTitle=tourTitle.strip();
        if(tourTitle.length()<1 || tourTitle.length()>16) throw new InvalidTourTitleFormatException();
        this.tourTitle=tourTitle;
    }

    public void setStartDate(LocalDateTime startDate){
        if(startDate==null) throw new InvalidTourDateException();
        startDate=startDate.with(LocalTime.MIN);
        if(endDate!=null && startDate.isAfter(endDate)) throw new InvalidTourDateException();
        this.startDate=startDate;
    }

    public void setEndDate(LocalDateTime endDate){
        if(endDate==null) throw new InvalidTourDateException();
        endDate=endDate.with(LocalTime.MIN);
        if(startDate!=null && startDate.isAfter(endDate)) throw new InvalidTourDateException();
        this.endDate=endDate;
    }
}
