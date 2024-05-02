package com.eminyidle.tour.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class CityEntity {
    @Id
    private Integer id;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "city_name_kor")
    private String cityNameKor;
    @Column(name = "city_name_eng")
    private String cityNameEng;

}
