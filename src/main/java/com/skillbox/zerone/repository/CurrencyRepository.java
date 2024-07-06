package com.skillbox.zerone.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.skillbox.zerone.entity.Currency;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Optional<Currency> findByName(String name);
}
