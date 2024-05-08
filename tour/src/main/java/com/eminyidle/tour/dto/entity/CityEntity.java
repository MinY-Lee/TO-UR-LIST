package com.eminyidle.tour.dto.entity;

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
@Entity(name = "city")
public class CityEntity {
    @Id
    @Column(name = "city_id")
    private Integer id;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "city_name_kor")
    private String cityNameKor;
    @Column(name = "city_name_eng")
    private String cityNameEng;

}
