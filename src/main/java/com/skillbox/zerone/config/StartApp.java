package com.skillbox.zerone.config;

import com.skillbox.zerone.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartApp {

    private final CurrencyService currencyService;

    @EventListener(ApplicationStartedEvent.class)
    public void initCurrencyStorage() {
        currencyService.updateCurrencyData();
    }
}
