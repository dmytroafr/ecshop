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


	public Map<String,Double> toMap (ResponseEntity<String> response){
		Pattern pattern = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+");
		Matcher matcher = pattern.matcher(response.toString());
		ArrayList<String> currencyList = new ArrayList<>();

		while (matcher.find()){
			currencyList.add(matcher.group());
		}
		return currencyList.stream()
				.collect(Collectors.toMap(s -> s.substring(1,4),s -> Double.parseDouble(s.substring(6))));
	}
	public ResponseEntity<String> getResponse(){
		String apiUrl = "https://v6.exchangerate-api.com/v6/3f905d8e5983cbdbe2a6bc4e/latest/USD";
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity(apiUrl, String.class);
	}
	@Override
	public String getUAH(Map<String,Double> map) {
		return "1 доллар США коштує " + map.get("UAH") + " грн";
	}

	@Override
	public String getEUR(Map<String,Double> map) {
		return "1 доллар США коштує " + map.get("EUR") + " євро";
	}

	@Override
	public String getPLN(Map<String,Double> map) {
		return "1 доллар США коштує " + map.get("PLN") + " злотих";
	}
}
