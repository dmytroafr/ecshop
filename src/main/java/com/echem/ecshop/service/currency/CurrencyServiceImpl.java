package com.echem.ecshop.service.currency;

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

@Service
public class CurrencyServiceImpl implements CurrencyService{

	@Value("${currency.url}")
	private String currencyUrl;


	public String getResponse(String currency){
		RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(currencyUrl+currency, String.class).toString();
	}

	public Map<String, Double> getMap(String currency){
		String response = getResponse(currency);
		Matcher matcher = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+")
				.matcher(response);
		List<String> currencyList = new ArrayList<>();

		while (matcher.find()){
			currencyList.add(matcher.group());
		}

        return currencyList.stream()
				.collect(Collectors.toMap(s -> s.substring(1, 4), s -> Double.parseDouble(s.substring(6))));
	}

	@Async
	@Cacheable(cacheNames = "currency", key = "#currency")
	@Override
	public CompletableFuture<String> getRate(String currency){
		return CompletableFuture.completedFuture("1 " + currency + " коштує " + getMap(currency).get("UAH") + " грн");
	}

}
