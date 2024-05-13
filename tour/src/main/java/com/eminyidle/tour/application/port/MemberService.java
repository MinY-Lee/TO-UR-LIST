package com.eminyidle.tour.application.port;

import com.eminyidle.tour.domain.Member;
import com.eminyidle.tour.application.dto.TourMember;
import com.eminyidle.tour.application.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.application.dto.req.DeleteMemberReq;
import com.eminyidle.tour.application.dto.req.UpdateGhostMemberReq;
import com.eminyidle.tour.application.dto.req.UpdateGhostToGuestReq;
import com.eminyidle.tour.application.dto.Ghost;


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
