package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.Country;
import com.eminyidle.tour.dto.CountryInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {

    @GetMapping()
    public ResponseEntity<List<Country>> searchCountryList(){
        return ResponseEntity.ok(new ArrayList<>());
    }


    @GetMapping("/{countryCode}")
    public ResponseEntity<List<CountryInfo>> searchCountryInfo(@PathVariable String countryCode){
        return ResponseEntity.ok(new ArrayList<>());
    }


    @GetMapping("/city/{countryCode}")
    public ResponseEntity<List<String>> searchCityNameList(@PathVariable String countryCode){
        return ResponseEntity.ok(new ArrayList<>());
    }
}
