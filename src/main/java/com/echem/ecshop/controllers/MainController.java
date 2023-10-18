package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.CurrencyService;
import com.echem.ecshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

	private final ProductService productService;
	private final CurrencyService currencyService;

	private static final Logger logger = LogManager.getRootLogger();
	public MainController(ProductService productService,CurrencyService currencyService) {
		this.productService = productService;
		this.currencyService = currencyService;
	}

	@RequestMapping({"","/"})
	public String index (Model model, HttpSession httpSession){
		logger.debug("request to /");
		logger.error("Алярм");
		String apiUrl = "https://v6.exchangerate-api.com/v6/3f905d8e5983cbdbe2a6bc4e/latest/USD";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
		model.addAttribute("currency", currencyService.getUAH((currencyService.toMap(response))));

		if (httpSession.getAttribute("myID") == null){
			String uuid = UUID.randomUUID().toString();
			httpSession.setAttribute("myID", uuid);
			System.out.println("Generated UUID ->"+uuid);
		}

		List<ProductDTO> list = productService.getAll();
		model.addAttribute("products", list);
		model.addAttribute("uuid", httpSession.getAttribute("myID"));

		return "index";
	}

	@RequestMapping ("/login")
	public String login(){
		logger.debug("Зайшли на сторінку логін");
		return "login";
	}

	@RequestMapping("/login-error") //щоб користувач потрапив на 404-page
	public String loginError(Model model) {

		model.addAttribute("login-error", true);
		return "login";
	}
	@RequestMapping ("/conditions")

	public String contditions(){
		logger.info("заходим сюди");
		return "conditions";
	}

	@RequestMapping("/about")
	public String about(){
		logger.info("request to /about");
		logger.debug("dddfs");
		return "about";};

	@RequestMapping("/contacts")
	public String contacts(){
		logger.debug("request to /contacts");

		return "contacts";};
}