package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.Country;
import com.eminyidle.tour.dto.CountryInfo;
import com.eminyidle.tour.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;
    @GetMapping()
    public ResponseEntity<List<Country>> searchCountryList(){

        return ResponseEntity.ok(countryService.searchCountryList());
    }

    @GetMapping("/{countryCode}")
    public ResponseEntity<CountryInfo> searchCountryInfo(@PathVariable String countryCode){
        return ResponseEntity.ok(countryService.searchCountryInfo(countryCode));
    }

    @GetMapping("/city/{countryCode}")
    public ResponseEntity<List<String>> searchCityNameList(@PathVariable String countryCode){
        return ResponseEntity.ok(
                countryService.searchCityList(countryCode).stream().map(
                        city -> city.getCityName()
                ).toList()
        );
    }
}
