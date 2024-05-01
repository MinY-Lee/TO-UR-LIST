package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.node.Activity;
import com.eminyidle.place.place.repository.ActivityRepository;
import com.eminyidle.place.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        String ans = placeRepository.findByTourActivityId(tourActivityId).get().toString();
        log.info(ans);

    }
}
