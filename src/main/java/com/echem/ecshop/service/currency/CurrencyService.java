package com.echem.ecshop.service.currency;


import java.util.concurrent.CompletableFuture;

public interface CurrencyService {
	CompletableFuture<String> getRate(String currencyCode);
}
