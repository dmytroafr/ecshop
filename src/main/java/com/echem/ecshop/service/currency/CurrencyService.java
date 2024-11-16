package com.echem.ecshop.service.currency;


import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CurrencyService {
	CompletableFuture<CurrencyRates> currencyRates();
}
