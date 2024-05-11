package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.*;
import com.eminyidle.tour.dto.req.*;
import com.eminyidle.tour.dto.Ghost;
import com.eminyidle.tour.dto.res.SearchTourDetailRes;
import com.eminyidle.tour.exception.UserInfoInRequestNotFoundException;
import com.eminyidle.tour.service.MemberService;
import com.eminyidle.tour.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;
    private final MemberService memberService;
    private final String HEADER_USER_ID="UserId";

    @PostMapping()
    public ResponseEntity<String> createTour(@RequestBody CreateTourReq createTourReq, @RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("createTour");
        Tour tour=tourService.createTour(userId,createTourReq);
        return ResponseEntity.ok(tour.getTourId());
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<SearchTourDetailRes> searchTourDetail(@PathVariable String tourId, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("searchTour");
        TourDetail tourDetail=tourService.searchTourDetail(userId, tourId);
        return ResponseEntity.ok(
                SearchTourDetailRes.builder()
                        .tourTitle(tourDetail.getTourTitle())
                        .startDate(tourDetail.getStartDate())
                        .endDate(tourDetail.getEndDate())
                        .cityList(tourDetail.getCityList())
                        .memberList(tourDetail.getMemberList())
                        .build()
        );
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteTour(@PathVariable String tourId, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("deleteTour "+tourId);
        tourService.deleteTour(userId, tourId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/title")
    public ResponseEntity<Void> updateTourTitle(@RequestBody UpdateTourTitleReq updateTourTitleReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourTitle ");
        tourService.updateTourTitle(userId, updateTourTitleReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/period")
    public ResponseEntity<Void> updateTourPeriod(@RequestBody UpdateTourPeriodReq updateTourPeriodReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourPeriod ");
        tourService.updateTourPeriod(userId, updateTourPeriodReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/city")
    public ResponseEntity<Void> updateTourCity(@RequestBody UpdateTourCityReq updateTourCityReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourCity ");
        tourService.updateTourCity(userId, updateTourCityReq);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<List<Tour>> searchTourList(@RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("searchTourList: "+userId);
        List<Tour> tourList=tourService.searchTourList(userId);
        return ResponseEntity.ok(tourList);
    }
    @DeleteMapping()
    public ResponseEntity<Void> quitTour(@RequestBody QuitTourReq quitTourReq, @RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("quitTour .. "+quitTourReq.getTourId());
        tourService.quitTour(userId, quitTourReq.getTourId());
        return ResponseEntity.ok().build();
    }


    //MEMBER 관련
    @PutMapping("/host")
    public ResponseEntity<Void> updateHost(@RequestBody TourMember tourMember, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.updateHost(userId,tourMember);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/{tourId}")
    public ResponseEntity<List<Member>> searchMemberList(@PathVariable String tourId, @RequestHeader(HEADER_USER_ID) String userId){
        List<Member> memberList=memberService.searchMemberList(userId,tourId);
        return ResponseEntity.ok(memberList);
    }

    @PostMapping("/member")
    public ResponseEntity<Void> createMember(@RequestBody TourMember tourMember, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.createMember(userId,tourMember);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/member/ghost")
    public ResponseEntity<Ghost> createGhostMember(@RequestBody CreateGhostMemberReq createGhostMemberReq, @RequestHeader(HEADER_USER_ID) String userId){
        Ghost ghost=memberService.createGhostMember(userId,createGhostMemberReq);
        return ResponseEntity.ok(ghost);
    }

    @PutMapping("/member/ghost")
    public ResponseEntity<Void> updateGhostMemberNickname(@RequestBody UpdateGhostMemberReq updateGhostMemberReq, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.updateGhostMemberNickname(userId,updateGhostMemberReq);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/member/resurrection")
    public ResponseEntity<Void> updateGhostToGuest(@RequestBody UpdateGhostToGuestReq updateGhostToGuestReq, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.updateGhostToGuest(userId,updateGhostToGuestReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteMemberReq deleteMemberReq, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.deleteMember(userId,deleteMemberReq);
        return ResponseEntity.ok().build();
    }

    //FEED
    //새 여행으로 가져오기 -> 피드번호에 맞는 tourId와 제목, 시작하는 날, 끝나는 날 알려주면...
        //그 도시 바탕으로 현재 유저에게 새로운 여행 만들어준다
        //도시는 알아서 그 도시로,
    //기존 여행으로 가져오기 -> 피드에 맞게 도시 "추가"
}
