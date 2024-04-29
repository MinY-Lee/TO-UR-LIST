package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;
import java.util.List;

@Node
@NoArgsConstructor
public class TourDetail extends Tour{
    // TODO - 상속과 롬복 잘 되는지 확인!
    //제목, 기간, 도시, 멤버
//    String tourId;
//    String tourTitle;
//    LocalDateTime startDate;
//    LocalDateTime endDate;
//    List<City> cityList;
    private List<Member> memberList;
    public TourDetail(String tourId,String tourTitle,LocalDateTime startDate,LocalDateTime endDate,List<City> cityList,List<Member> memberList){
        super(tourId,tourTitle,startDate,endDate,cityList);
        setMemberList(memberList);
    }

    public List<Member> getMemberList(){
        return memberList;
    }

    public void setMemberList(List<Member> memberList){
        this.memberList=memberList;
    }

    @Override
    public String toString(){
        return "TourDetail: "+super.toString()+" "+getMemberList();
    }

}
