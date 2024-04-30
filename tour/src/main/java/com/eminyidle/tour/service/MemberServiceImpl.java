package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.TourMember;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.dto.req.DeleteMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostToGuestReq;
import com.eminyidle.tour.exception.NoSuchTourException;
import com.eminyidle.tour.repository.UserRepository;

import java.util.List;

public class MemberServiceImpl implements MemberService {

    UserRepository userRepository;

    private boolean isHost(String userId, String tourId) {
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if ("host".equals(memberType)) {
            return true;
        }
        return false;
    }

    @Override
    public void createMember(String hostId, TourMember tourMember) {
        User user = userRepository.findById(tourMember.getUserId())
                .orElse(userRepository.save(
                        //TODO - 나의 노드에 없을 때, 올바른 유저인지 확인 필요
                        User.builder().userId(tourMember.getUserId()).userNickname(tourMember.getUserNickname()).build()
                ));

        userRepository.createGuestRelationship(hostId, tourMember.getTourId(), user.getUserId());
    }

    @Override
    public void createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq) {
        userRepository.createMemberRelationship(createGhostMemberReq.getGhostNickname(), createGhostMemberReq.getTourId(), "ghost");
    }

    @Override
    public List<Member> searchMemberList(String tourId) {
        List<Member> memberList = userRepository.findMembersByTourId(tourId);
        if (memberList.isEmpty()) throw new NoSuchTourException();
        return memberList;
    }

    @Override
    public void updateGhostToGuest(String hostId, UpdateGhostToGuestReq updateGhostToGuestReq) {

    }

    @Override
    public void updateHost(String hostId, TourMember tourMember) {
        User user = userRepository.findById(tourMember.getUserId())
                .orElse(userRepository.save(
                        //TODO - 나의 노드에 없을 때, 올바른 유저인지 확인 필요
                        User.builder().userId(tourMember.getUserId()).userNickname(tourMember.getUserNickname()).build()
                ));

        userRepository.updateMemberRelationshipExceptGhost(hostId, tourMember.getTourId(), "guest");
        userRepository.updateMemberRelationshipExceptGhost(user.getUserId(), tourMember.getTourId(), "host");
    }

    @Override
    public void deleteMember(String hostId, DeleteMemberReq deleteMemberReq) {
        userRepository.deleteMemberRelationship(deleteMemberReq.getUserId(), deleteMemberReq.getTourId(), deleteMemberReq.getMemberType());
    }
}
