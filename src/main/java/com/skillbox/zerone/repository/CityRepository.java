package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.City;
import com.skillbox.zerone.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCityByCountry(Country country);

    List<City> findCityByCountryAndNameStartsWith(Country country, String starts);

    Optional<City> findCityByName(String name);
}
