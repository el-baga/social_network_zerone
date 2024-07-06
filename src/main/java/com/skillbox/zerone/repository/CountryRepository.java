package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findCountryByName(String name);
}
