package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.*;
import com.eminyidle.tour.dto.req.*;
import com.eminyidle.tour.dto.res.Ghost;
import com.eminyidle.tour.dto.res.SearchTourDetailRes;
import com.eminyidle.tour.exception.UserInfoInRequestNotFoundException;
import com.eminyidle.tour.service.MemberService;
import com.eminyidle.tour.service.TourService;
import com.eminyidle.tour.service.TourServiceImpl;
import com.eminyidle.tour.service.UserService;
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
    private final String HEADER_USER_ID="userId";

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
    @DeleteMapping() //TODO - 이렇게 정의하는게 맞을지 체크
    public ResponseEntity<Void> quitTour(@RequestBody String tourId, @RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("quitTour");
        return ResponseEntity.ok().build();
    }


    //MEMBER 관련
    @PutMapping("/host")
    public ResponseEntity<Void> updateHost(@RequestBody TourMember tourMember, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.updateHost(userId,tourMember);
        //TODO - 메서드를 boolean으로 선언해서 성공 여부를 확인해야 할까?
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
        //TODO - 메서드를 boolean으로 선언해서 성공 여부를 확인해야 할까?
        return ResponseEntity.ok().build();
    }
    @PostMapping("/member/ghost")
    public ResponseEntity<Ghost> createGhostMember(@RequestBody CreateGhostMemberReq createGhostMemberReq, @RequestHeader(HEADER_USER_ID) String userId){
        Ghost ghost=memberService.createGhostMember(userId,createGhostMemberReq);
        return ResponseEntity.ok(ghost);
    }
    @PostMapping("/member/resurrection")
    public ResponseEntity<Void> updateGhostToGuest(@RequestBody UpdateGhostToGuestReq updateGhostToGuestReq, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.updateGhostToGuest(userId,updateGhostToGuestReq);
        //TODO - 메서드를 boolean으로 선언해서 성공 여부를 확인해야 할까?
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteMemberReq deleteMemberReq, @RequestHeader(HEADER_USER_ID) String userId){
        memberService.deleteMember(userId,deleteMemberReq);
        //TODO - 메서드를 boolean으로 선언해서 성공 여부를 확인해야 할까?
        return ResponseEntity.ok().build();
    }

    private User getUserFromHeader(Map<String,String> header){
        try {
            return User.builder()
                    .userId(header.get("userid"))
                    .userNickname(URLDecoder.decode(header.get("usernickname"),"UTF-8"))
                    .userName(URLDecoder.decode(header.get("username"),"UTF-8"))
                    .tourList(new ArrayList<>())
                    .build();
        } catch (UnsupportedEncodingException | NullPointerException e) {
            throw new UserInfoInRequestNotFoundException(e.getMessage());
        }
    }
}
