package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.TourMember;
import com.eminyidle.tour.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.dto.req.DeleteMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostToGuestReq;
import com.eminyidle.tour.dto.Ghost;


import java.util.List;

public interface MemberService {

    void createMember(String hostId, TourMember tourMember);

    Ghost createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq);
    void updateGhostMemberNickname(String userId, UpdateGhostMemberReq updateGhostMemberReq);
    List<Member> searchMemberList(String userId,String tourId);

    void updateGhostToGuest(String hostId, UpdateGhostToGuestReq updateGhostToGuestReq);

    void updateHost(String hostId, TourMember tourMember);

    void deleteMember(String hostId, DeleteMemberReq deleteMemberReq);

}
