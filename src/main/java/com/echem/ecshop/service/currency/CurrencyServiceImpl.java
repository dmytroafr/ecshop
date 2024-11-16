package com.echem.ecshop.service.currency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

	private final CacheManager cacheManager;
	private final RestTemplate restTemplate;

	@Value("${currency.url}")
	private String currencyUrl;

	public CurrencyServiceImpl(CacheManager cacheManager, RestTemplate restTemplate) {
		this.cacheManager = cacheManager;
		this.restTemplate = restTemplate;
	}

	@Async
	@Cacheable(cacheNames = "Currencies", key = "'Currencies'")
	@Override
	public CompletableFuture<CurrencyRates> currencyRates() {
		return CompletableFuture.supplyAsync(this::getResponse);
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateCache() {
		log.info("Updating currency cache");
		try {
			CurrencyRates rates = getResponse();
			Cache currencies = cacheManager.getCache("Currencies");
            assert currencies != null;
            currencies.put("Currencies", rates);
			log.info("Currency cache updated successfully.");
		} catch (Exception e) {
			log.error("Failed to update currency cache", e);
		}
	}

	private CurrencyRates getResponse() {
		try {
			log.debug("CurrencyService: Sending request to currency API");
			ResponseEntity<CurrencyRates> response = restTemplate.getForEntity(currencyUrl, CurrencyRates.class);
			log.debug("CurrencyService: Received response from currency API");
			return response.getBody();
		} catch (Exception e) {
			log.error("Error occurred while fetching currency rates", e);
			throw new RuntimeException("Failed to fetch currency rates", e);
		}
	}
}

