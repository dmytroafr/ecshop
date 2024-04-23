package com.echem.ecshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService{

	@Value("${currency.url}")
	private String currencyUrl;



	@Async
	@Cacheable(cacheNames = "currency",key = "#currency")
	public String getResponse(String currency){
		var restTemplate = new RestTemplate();
        return restTemplate.getForEntity(currencyUrl+currency, String.class).toString();
	}

	public Map<String, Double> getMap(String currency){
		var response = getResponse(currency);
		return toMap(response);
    }
	private Map<String,Double> toMap (String response){
		var matcher = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+")
				.matcher(response);
		var currencyList = new ArrayList<String>();
		while (matcher.find()){
			currencyList.add(matcher.group());
		}
		return currencyList.stream()
				.collect(Collectors.toMap(s -> s.substring(1,4),s -> Double.parseDouble(s.substring(6))));
	}

	@Override
	public String getUAH(){
		return "1 доллар США коштує " + getMap("USD").get("UAH") + " грн";
	}

	@Override
	public String getEUR() {
		return "1 євро коштує " + getMap("EUR").get("UAH") + " грн";
	}

	@Override
	public String getPLN() {
		return "1 польський злотий коштує " + getMap("PLN").get("UAH") + " грн";
	}
}
