package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Member;
import com.eminyidle.tour.dto.TourMember;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.dto.req.DeleteMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostToGuestReq;
import com.eminyidle.tour.dto.res.Ghost;
import com.eminyidle.tour.exception.HostCanNotBeDeletedException;
import com.eminyidle.tour.exception.NoHostPrivilegesException;
import com.eminyidle.tour.exception.NoSuchTourException;
import com.eminyidle.tour.exception.UserNotAttendSuchTourException;
import com.eminyidle.tour.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    UserRepository userRepository;

    private void assertHost(String userId, String tourId) {
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if (!"host".equals(memberType)) {
            throw new NoHostPrivilegesException();
        }
    }

    private void assertAttend(String userId, String tourId) {
        userRepository.findUserByAttendRelationship(userId, tourId).orElseThrow(UserNotAttendSuchTourException::new);
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
    public Ghost createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq) {
        userRepository.createMemberRelationship(createGhostMemberReq.getGhostNickname(), createGhostMemberReq.getTourId(), "ghost");
        return Ghost.builder().build();
    }

    @Override
    public List<Member> searchMemberList(String userId, String tourId) {
        assertAttend(userId, tourId);

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
        if ("host".equals(deleteMemberReq.getMemberType())) {
            throw new HostCanNotBeDeletedException();
        }
        //i: 실행여부
        int i = userRepository.deleteMemberRelationship(deleteMemberReq.getUserId(), deleteMemberReq.getTourId(), deleteMemberReq.getMemberType());
        log.debug(">>deleted>>" + i);
        if (i == 0) {
            //실행되지 않음. userId, tourId, memberType 뭔가 잘못된 것
        }
        //TODO- 모든 아이템 삭제 요청
        // 이용 ->deleteMemberReq.getUserId(), deleteMemberReq.getTourId()
    }
}
