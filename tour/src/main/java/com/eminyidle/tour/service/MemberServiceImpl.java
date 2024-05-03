package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.*;
import com.eminyidle.tour.dto.req.CreateGhostMemberReq;
import com.eminyidle.tour.dto.req.DeleteMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostMemberReq;
import com.eminyidle.tour.dto.req.UpdateGhostToGuestReq;
import com.eminyidle.tour.dto.Ghost;
import com.eminyidle.tour.exception.*;
import com.eminyidle.tour.repository.GhostRepository;
import com.eminyidle.tour.repository.TourRepository;
import com.eminyidle.tour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final GhostRepository ghostRepository;

    private void assertHost(String userId, String tourId) {
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if (!"host".equals(memberType)) {
            throw new NoHostPrivilegesException();
        }
    }

    private void assertAttend(String userId, String tourId) {
        userRepository.findUserByAttendRelationship(userId, tourId).orElseThrow(UserNotAttendSuchTourException::new);
    }

    //TODO - 바꿀 수 있으면.. 에러 없이 하는 방법...으로,....
    private boolean isAttend(String userId, String tourId){
        return userRepository.existsAttendRelationshipByUserIdAndTourId(userId,tourId);
    }

    @Override
    public void createMember(String hostId, TourMember tourMember) {
        assertHost(hostId,tourMember.getTourId());
        if(isAttend(tourMember.getUserId(),tourMember.getTourId())){
            throw new AlreadyUserAttendTourException();
        }
        Tour tour = tourRepository.findById(tourMember.getTourId()).orElseThrow(NoSuchTourException::new);
        User user = userRepository.findById(tourMember.getUserId())
                .orElse(userRepository.save(
                        //TODO - 나의 노드에 없을 때, user서비스에 요청보내기
                        //  없는 유저라면 exception
                        User.builder()
                                .userId(tourMember.getUserId())
                                .tourList(new ArrayList<>())
                                .userName("서버잉")
                                .userNickname("servering")
                                .build()
                ));
        userRepository.createGuestRelationship(hostId, tour.getTourId(), user.getUserId());
    }

    @Override
    public Ghost createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq) {
        assertHost(hostId, createGhostMemberReq.getTourId());

        Ghost existingGhost=ghostRepository.findByGhostNicknameAndTourId(createGhostMemberReq.getGhostNickname(), createGhostMemberReq.getTourId());
        if(existingGhost!=null){
            throw new DuplicatedGhostNicknameException();
        }

        Ghost ghost = ghostRepository.save(Ghost.builder()
                .ghostId(UUID.randomUUID().toString())
                .ghostNickname(createGhostMemberReq.getGhostNickname())
                .build());
        ghostRepository.createGhostRelationship(ghost.getGhostId(), createGhostMemberReq.getTourId());
        return ghost;
    }

    @Override
    public void updateGhostMemberNickname(String hostId, UpdateGhostMemberReq updateGhostMemberReq) {
        assertHost(hostId, updateGhostMemberReq.getTourId());

        Ghost ghost=ghostRepository.findById(updateGhostMemberReq.getGhostId()).orElseThrow(NoSuchGhostException::new);
        ghost.setGhostNickname(updateGhostMemberReq.getGhostNickname());
        ghostRepository.save(ghost);
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
        Ghost ghost=ghostRepository.findById(updateGhostToGuestReq.getGhostId()).orElseThrow(NoSuchGhostException::new);
        User user=userRepository.findById(updateGhostToGuestReq.getUserId()).orElseThrow(NoSuchUserException::new);

        //여기에서 host임이 보장된다
        createMember(hostId,TourMember.builder()
                .userId(user.getUserId())
                .tourId(updateGhostToGuestReq.getTourId())
                .userNickname(user.getUserNickname())
                .build()
        );
        //TODO- ghost에 있던 연결 guest에 잇기
        ghostRepository.delete(ghost);
    }

    @Override
    public void updateHost(String hostId, TourMember tourMember) {
        assertHost(hostId,tourMember.getTourId());

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
        assertHost(hostId, deleteMemberReq.getTourId());

        switch (deleteMemberReq.getMemberType()){
            case "host":
                throw new HostCanNotBeDeletedException();
            case "guest":
                userRepository.deleteMemberRelationship(deleteMemberReq.getUserId(), deleteMemberReq.getTourId(), deleteMemberReq.getMemberType());
                //TODO- 모든 아이템 삭제 요청
                // 이용 ->deleteMemberReq.getUserId(), deleteMemberReq.getTourId()
                break;
            case "ghost":
                ghostRepository.deleteById(deleteMemberReq.getUserId());
                break;
            default:
                throw new InvalidMemberTypeException();
        }
    }
}
