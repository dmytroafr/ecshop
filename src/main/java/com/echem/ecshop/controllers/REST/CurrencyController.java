package com.echem.ecshop.controllers.REST;

import com.echem.ecshop.service.currency.CurrencyRates;
import com.echem.ecshop.service.currency.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/v1/update-currency")
    @ResponseBody
    public String updateCurrency(@RequestParam String currency) throws ExecutionException, InterruptedException {
        log.info("Отримано запит для валюти: {}", currency);
        CurrencyRates rates = currencyService.currencyRates().get();

        if (rates == null || rates.getConversionRates() == null) {
            log.error("Курси валют відсутні!");
            return "Невідомо";
        }

        Double rateValue = rates.getConversionRates().get(currency);
        log.info("Курс для {}: {}", currency, rateValue);

        if (rateValue == null) {
            return "Невідомо";
        }
        return String.format("%.2f", rateValue * 100);
    }

}
