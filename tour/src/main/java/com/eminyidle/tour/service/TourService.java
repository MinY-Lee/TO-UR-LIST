package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Tour;
import com.eminyidle.tour.dto.TourDetail;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateTourReq;
import com.eminyidle.tour.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.dto.req.UpdateTourTitleReq;

import java.util.List;

public interface TourService {

    Tour createTour(User user, CreateTourReq createTourReq);

    void deleteTour(String tourId);

    void updateTourTitle(String userId, UpdateTourTitleReq updateTourTitleReq);

    void updateTourPeriod(String userId, UpdateTourPeriodReq updateTourPeriodReq);

    void updateTourCity(String userId, UpdateTourCityReq updateTourCityReq);

    Tour searchTour(String userId, String tourId);
    TourDetail searchTourDetail(String userId, String tourId);

    List<Tour> searchTourList(String userId);

    void quitTour(String userId, String tourId);
}
