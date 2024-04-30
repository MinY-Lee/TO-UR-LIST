package com.eminyidle.tour.dto.req;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTourTitleReq {
    String tourId;
    String tourTitle;
}
