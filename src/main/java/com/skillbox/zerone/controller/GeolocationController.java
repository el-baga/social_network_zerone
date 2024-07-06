package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.response.GeolocationRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.service.GeolocationService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/geolocations")
public class GeolocationController {

    private final GeolocationService geolocationService;

    @EndpointDescription(summary = "Get countries")
    @GetMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<GeolocationRs>> getCountries() {
        return geolocationService.getCountries();
    }

    @EndpointDescription(summary = "")
    @GetMapping(value = "/cities/uses", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<GeolocationRs> getCitiesUses(@RequestParam String country)  {
        return geolocationService.getCitiesUses(country);
    }

    @EndpointDescription(summary = "")
    @GetMapping(value = "/cities/db", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<GeolocationRs>> getCitiesDb(@RequestParam String country, @RequestParam String starts)  {
        return geolocationService.getCitiesDb(country, starts);
    }

    @EndpointDescription(summary = "")
    @GetMapping(value = "/cities/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<GeolocationRs>> getCitiesApi(@RequestParam String country, @RequestParam String starts)
                                                                        throws ClientException, ApiException {
        return geolocationService.getCitiesApi(country, starts);
    }

}
