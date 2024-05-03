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

    @Override
    public void searchTourActivityByPlaceId(String tourActivityId) {
        log.info("서비스 실행");
//        String ans = placeRepository.findByTourActivityId(tourActivityId).get().getActivity().getActivity().toString();
        String ans = placeRepository.findByTourActivityId(tourActivityId).get().toString();
        log.info(ans);

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
            activityRepository.createReferenceRelationship(tourId, placeId, tourDay, activity);
            return true;
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
}
