package com.skillbox.zerone.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.skillbox.zerone.entity.Weather;


@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    Optional<Weather> findByCity(String city);
}
