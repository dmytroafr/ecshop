package com.echem.ecshop.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService{

	private ResponseEntity<String> getResponse(){
		String apiUrl = "https://v6.exchangerate-api.com/v6/3f905d8e5983cbdbe2a6bc4e/latest/USD";
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity(apiUrl, String.class);
	}
	private Map<String, Double> getMap() {
		ResponseEntity<String> response = getResponse();
        return toMap(response);
	}
	private Map<String,Double> toMap (ResponseEntity<String> response){
		Pattern pattern = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+");
		Matcher matcher = pattern.matcher(response.toString());
		ArrayList<String> currencyList = new ArrayList<>();

		while (matcher.find()){
			currencyList.add(matcher.group());
		}
		return currencyList.stream()
				.collect(Collectors.toMap(s -> s.substring(1,4),s -> Double.parseDouble(s.substring(6))));
	}



	@Override
	public String getUAH(){
		return "1 доллар США коштує " + getMap().get("UAH") + " грн";
	}

	@Override
	public String getEUR() {
		return "1 доллар США коштує " + getMap().get("EUR") + " євро";
	}

	@Override
	public String getPLN() {
		return "1 доллар США коштує " + getMap().get("PLN") + " злотих";
	}
}
