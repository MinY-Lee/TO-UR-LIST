package com.eminyidle.tour.application.port;

import com.eminyidle.tour.application.dto.Tour;
import com.eminyidle.tour.application.dto.TourDetail;
import com.eminyidle.tour.application.dto.req.CreateTourReq;
import com.eminyidle.tour.application.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.application.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.application.dto.req.UpdateTourTitleReq;

import java.util.List;

public interface TourService {

    Tour createTour(String userId, CreateTourReq createTourReq);

    void deleteTour(String userId, String tourId);

    void updateTourTitle(String userId, UpdateTourTitleReq updateTourTitleReq);

    void updateTourPeriod(String userId, UpdateTourPeriodReq updateTourPeriodReq);

    void updateTourCity(String userId, UpdateTourCityReq updateTourCityReq);

    Tour searchTour(String userId, String tourId);
    TourDetail searchTourDetail(String userId, String tourId);

    List<Tour> searchTourList(String userId);

    void quitTour(String userId, String tourId);
}
