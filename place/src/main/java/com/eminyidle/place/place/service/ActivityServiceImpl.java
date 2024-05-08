package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.node.Activity;
import com.eminyidle.place.place.repository.ActivityRepository;
import com.eminyidle.place.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final PlaceRepository placeRepository;

    @Override
    public List<String> searchActivityList() {
        List<String> allActivity = new ArrayList<>();
        for (Activity activity : activityRepository.findAll()) {
            allActivity.add(activity.getActivity());
        }
        return activityRepository.findAll().stream().map(activity -> activity.getActivity()).toList();
    }

    // 활동 추가
    @Override
    public boolean addActivity(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers) {
        // 받아 온 body = requestbody
        String placeId = (String) body.get("placeId");
        Integer tourDay = (Integer) body.get("tourDay");
        String activity = (String) body.get("activity");
        boolean isSuccess = false;
        if (checkActivityDuplication(tourId, placeId, tourDay, activity)){
            // REFERENCE 관계 생성해주기
            try {
                activityRepository.createReferenceRelationship(tourId, placeId, tourDay, activity);
                return true;
            } catch (Exception e) {
                log.info("REFERENCE 관계 생성 오류");
                return false;
            }
        }
        return false;
    }

    // 중복된 활동 확인
    @Override
    public Boolean checkActivityDuplication(String tourId, String placeId, Integer tourDay, String activity) {
        try {
            activityRepository.findActivityByTourIdAndPlaceIdAndActivityId(tourId, placeId, tourDay, activity).orElseThrow(NoSuchElementException ::new);
            log.info("중복된 활동이 있음");
            return false;
        }
        catch (NoSuchElementException e) {
            log.info("중복된 활동이 없음");
            return true;
        } catch (Exception e) {
            log.info("활동 확인 과정에서 오류 발생");
            return false;
        }
    }

    // 활동 삭제
    @Override
    public boolean deleteActivity(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> simpSessionAttributes) {
        // 받아 온 body = requestbody
        String tourPlaceId = (String) body.get("tourPlaceId");
        String activity = (String) body.get("activity");
        // 냅다 삭제를 갈기고 오류가 나면 잡아주기
        try {
            activityRepository.deleteByTourPlaceIdAndActivity(tourPlaceId, activity);
            log.info("해당하는 활동 삭제");
            return true;
        } catch (NoSuchElementException e) {
            log.info("해당하는 활동이 없음");
            return false;
        } catch (Exception e) {
            log.info("활동 삭제 과정에서 오류 발생");
            return false;
        }
    }

    // 등록한 활동 조회
    @Override
    public List<String> searchEnrollActivity(String tourPlaceId) {
        log.info("등록된 활동 찾기");
//        String ans = placeRepository.findByTourActivityId(tourActivityId).get().getActivity().getActivity().toString();
        List<String> enrollActivity = placeRepository.findByTourPlaceId(tourPlaceId).get().getActivityList().stream().map(activity -> activity.getActivity()).toList();
        log.info(enrollActivity.toString());
        return enrollActivity;
    }
}