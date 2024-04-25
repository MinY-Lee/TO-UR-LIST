package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.Activity;
import com.eminyidle.place.place.repository.ActivityRepository;
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

    @Override
    public List<String> searchActivityList() {
        List<String> allActivity = new ArrayList<>();
        for (Activity activity : activityRepository.findAll()) {
            allActivity.add(activity.getActivity());
        }
        return activityRepository.findAll().stream().map(activity -> activity.getActivity()).toList();
    }
}
