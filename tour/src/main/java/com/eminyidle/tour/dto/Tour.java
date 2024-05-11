package com.eminyidle.tour.dto;

import com.eminyidle.tour.exception.InvalidTourDateException;
import com.eminyidle.tour.exception.InvalidTourTitleFormatException;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Node(primaryLabel = "TOUR")
@Getter
@NoArgsConstructor
@Builder
@ToString
public class Tour {
    //제목, 기간, 도시
    @Id @Setter
    String tourId;
    @Property(readOnly = true)
    String tourTitle;

    LocalDateTime startDate;
    @Property
    LocalDateTime endDate;
    @Relationship(type = "TO") @Setter
    List<City> cityList;

    public Tour(String tourId, String tourTitle, LocalDateTime startDate, LocalDateTime endDate, List<City> cityList) {
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
