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

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final GhostRepository ghostRepository;
    private final RequestService requestService;

    private void assertHost(String userId, String tourId) {
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if (!"host".equals(memberType)) {
            throw new NoHostPrivilegesException();
        }
    }

    private void assertAttend(String userId, String tourId) {
        userRepository.findUserByAttendRelationship(userId, tourId).orElseThrow(UserNotAttendSuchTourException::new);
    }

    private boolean isAttend(String userId, String tourId) {
        return userRepository.existsAttendRelationshipByUserIdAndTourId(userId, tourId);
    }

    private boolean isExistUser(String userId) {
        return userRepository.existsById(userId);
    }

    //Refactor하면 좋을듯..
    @Override
    public void createMember(String hostId, TourMember tourMember) {
        assertHost(hostId, tourMember.getTourId());
        if (isExistUser(tourMember.getUserId()) && isAttend(tourMember.getUserId(), tourMember.getTourId())) {
            throw new AlreadyUserAttendTourException();
        }
        Tour tour = tourRepository.findById(tourMember.getTourId()).orElseThrow(NoSuchTourException::new);
        User user = userRepository.findById(tourMember.getUserId())
                .orElseGet(() ->
                        userRepository.save(
                                requestService.getUser(tourMember.getUserId())
                        )
                );
        userRepository.createGuestRelationship(hostId, tour.getTourId(), user.getUserId());
        // TODO - 연결된 모든 체크리스트 생성 (Kafka)
    }

    @Override
    public Ghost createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq) {
        assertHost(hostId, createGhostMemberReq.getTourId());

        if (ghostRepository.existsGhostByGhostNicknameAndTourId(createGhostMemberReq.getGhostNickname(), createGhostMemberReq.getTourId())) {
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

        Ghost ghost = ghostRepository.findById(updateGhostMemberReq.getGhostId()).orElseThrow(NoSuchGhostException::new);
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
        Ghost ghost = ghostRepository.findById(updateGhostToGuestReq.getGhostId()).orElseThrow(NoSuchGhostException::new);

        //여기에서 host임이 보장된다
        createMember(hostId, TourMember.builder()
                .userId(updateGhostToGuestReq.getUserId())
                .tourId(updateGhostToGuestReq.getTourId())
                .userNickname(updateGhostToGuestReq.getUserNickname())
                .build()
        );
        //TODO- ghost에 있던 연결(가계부) guest에 잇기 : Kafka or RestTemplate
        ghostRepository.delete(ghost);
    }

    @Override
    public void updateHost(String hostId, TourMember tourMember) {
        assertHost(hostId, tourMember.getTourId());

        //참여중인 유저가 아니라면, 권한을 넘겨줄 수 없다.
        User user = userRepository.findUserByAttendRelationship(tourMember.getUserId(), tourMember.getTourId()).orElseThrow(NoSuchMemberException::new);

        userRepository.updateMemberRelationshipExceptGhost(hostId, tourMember.getTourId(), "guest");
        userRepository.updateMemberRelationshipExceptGhost(user.getUserId(), tourMember.getTourId(), "host");

    }

    @Override
    public void deleteMember(String hostId, DeleteMemberReq deleteMemberReq) {
        assertHost(hostId, deleteMemberReq.getTourId());

        switch (deleteMemberReq.getMemberType()) {
            case "host":
                throw new HostCanNotBeDeletedException();
            case "guest":
                userRepository.deleteGuestRelationship(deleteMemberReq.getUserId(), deleteMemberReq.getTourId());
                //TODO- 모든 아이템 삭제 요청(KAFKA)
                // 이용 ->deleteMemberReq.getUserId(), deleteMemberReq.getTourId()
                break;
            case "ghost":
                ghostRepository.deleteById(deleteMemberReq.getUserId());
                break;
            default:
                throw new UndefinedMemberTypeException();
        }
    }
}
