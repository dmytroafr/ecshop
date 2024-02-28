package com.echem.ecshop.service;


import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CurrencyService {
	Map<String,Double> toMap (ResponseEntity<String> response);
	ResponseEntity<String> getResponse();
	String getUAH(Map<String,Double> map);
	String getEUR(Map<String,Double> map);
	String getPLN(Map<String,Double> map);

}
