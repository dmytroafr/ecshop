package com.echem.ecshop.service.currency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService{

	@Value("${currency.url}")
	private String currencyUrl;

	private String getResponse(String currency){
		log.info("CurrencyService: Get currency response");
		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.getForEntity(currencyUrl + currency, String.class).toString();
		log.info("CurrencyService: Received response with currencies");
		return string;
	}

	private Map<String, Double> getMap(String currency){
		String response = getResponse(currency);
		Matcher matcher = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+")
				.matcher(response);
		List<String> currencyList = new ArrayList<>();
		while (matcher.find()){
			currencyList.add(matcher.group());
		}
		Map<String, Double> collect = currencyList.stream()
				.collect(Collectors.toMap(s -> s.substring(1, 4), s -> Double.parseDouble(s.substring(6))));
		log.info("CurrencyService: Mapped Response string into Map");
		return collect;
	}

	@Async
	@Cacheable(cacheNames = "currency", key = "#currency")
	@Override
	public CompletableFuture<String> getRate(String currency){
		log.info("CurrencyService: Try to get rate of {}", currency);
		CompletableFuture<String> rate = CompletableFuture
				.completedFuture("1 " + currency + " коштує " + getMap(currency).get("UAH") + " грн");
		log.info("CurrencyService: Got rate of {}", currency);
		return rate;
	}
}
