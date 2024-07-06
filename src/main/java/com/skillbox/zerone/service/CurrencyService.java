package com.skillbox.zerone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skillbox.zerone.entity.Currency;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.CurrencyRepository;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String JSON_KEY = "Valute";

    private final CurrencyRepository currencyRepository;

    public void updateCurrencyData() {
        final JsonNode jResp = getCurrencyFromBank();
        if (jResp == null) {
            return;
        }
        saveCurrency("USD", "Доллар США", jResp);
        saveCurrency("EUR", "Евро", jResp);
    }

    public String getCurrency(String currencyName) {
        return currencyRepository.findByName(currencyName).orElseThrow(
                () -> new BadRequestException("Валюта с названием: " + currencyName + " в базе данных не найдена")).getPrice();
    }

    private void saveCurrency(String charCode, String currencyName, JsonNode jResp) {
        Optional<Currency> optionalCurrency = currencyRepository.findByName(currencyName);
        Currency currency = new Currency();
        if (optionalCurrency.isPresent()) {
            currency = optionalCurrency.get();
        }
        currency.setName(jResp.get(JSON_KEY).get(charCode).get("Name").asText());
        double price = jResp.get(JSON_KEY).get(charCode).get("Value").asDouble();
        currency.setPrice(String.format("%.2f", price));
        currency.setUpdateTime(LocalDateTime.now());
        currencyRepository.save(currency);
    }

    private JsonNode getCurrencyFromBank() {
        return CommonUtil.getFromWeb("https://www.cbr-xml-daily.ru/daily_json.js");
    }
}
