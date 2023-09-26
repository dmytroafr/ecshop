package com.echem.ecshop.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CurrencyServiceImpl implements CurrencyService{

	Map<String,Double> currencyMap = new HashMap<>();


	public Map<String,Double> toMap (ResponseEntity<String> response){

		Pattern pattern = Pattern.compile("\"[A-Z]{3}\":[0-9]+.[0-9]+");
		Matcher matcher = pattern.matcher(response.toString());
		ArrayList<String> currencyMap = new ArrayList<>();

		while (matcher.find()){
			currencyMap.add(matcher.group());
		}
		for (int i = 0; i < currencyMap.size(); i++) {
			this.currencyMap.put(currencyMap.get(i).substring(1,4),Double.parseDouble(currencyMap.get(i).substring(6)));
		}
		return this.currencyMap;
	};


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
