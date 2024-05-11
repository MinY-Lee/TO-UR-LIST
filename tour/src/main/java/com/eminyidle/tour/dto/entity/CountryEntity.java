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
@Entity(name = "country")
public class CountryEntity {
    @Id
    @Column(name = "country_code")
    String countryCode;
    @Column(name = "country_name_kor")
    String countryNameKor;
    @Column(name = "country_name_eng")
    String countryNameEng;

    @Column(name = "climate")
    String climate;
    @Column(name = "language")
    String language;
    @Column(name = "voltage")
    String voltage;
    @Column(name = "plug_type")
    String plug_type;
    @Column(name = "utc")
    Double utc;
}
