package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.TourMember;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.dto.req.DeleteMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostToGuestReq;


import java.util.List;

public interface MemberService {

    //TODO - user서비스에서 해야 하는지 확인...
//    List<User> searchUserListByNickname(String userNickname);

    void createMember(String hostId, TourMember tourMember);

    void createGhostMember(String hostId, CreateGhostMemberReq
            createGhostMemberReq
    );

    List<Member> searchMemberList(String tourId);

    void updateGhostToGuest(String hostId, UpdateGhostToGuestReq updateGhostToGuestReq);

    void updateHost(String hostId, TourMember tourMember);

    void deleteMember(String hostId, DeleteMemberReq deleteMemberReq);
}
