package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.ProductService;
import com.echem.ecshop.service.SessionObjectHolder;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

	private final SessionObjectHolder sessionObjectHolder;
	private final ProductService productService;

	private final Logger logger = LoggerFactory.getLogger(MainController.class);
	public MainController(SessionObjectHolder sessionObjectHolder, ProductService productService) {
		this.productService = productService;
		this.sessionObjectHolder = sessionObjectHolder;
	}

	@RequestMapping({"","/"})
	public String index (Model model, HttpSession httpSession){
		logger.info("отримали запит і перевели на індекс");

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
		logger.info("Зайшли на сторінку логін");
		return "login";
	}

	@RequestMapping("/login-error") //щоб користувач потрапив на 404-page
	public String loginError(Model model) {

		model.addAttribute("login-error", true);
		return "login";
	}
	@RequestMapping ("/conditions")
	public String contditions(){
		return "conditions";
	}

	@RequestMapping("/about")
	public String about(){return "about";};

	@RequestMapping("/contacts")
	public String contacts(){return "contacts";};
}