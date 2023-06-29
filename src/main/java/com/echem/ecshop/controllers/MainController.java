package com.echem.ecshop.controllers;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.SessionObjectHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class MainController {

	private final SessionObjectHolder sessionObjectHolder;

	public MainController(SessionObjectHolder sessionObjectHolder) {
		this.sessionObjectHolder = sessionObjectHolder;
	}

	@RequestMapping({"","/"})
	public String index (Model model, HttpSession httpSession){
		model.addAttribute("amountClicks", sessionObjectHolder.getAmountClicks());
		if (httpSession.getAttribute("myID") == null){
			String uuid = UUID.randomUUID().toString();
			httpSession.setAttribute("myID", uuid);
			System.out.println("Generated UUID ->"+uuid);
		}
		model.addAttribute("uuid", httpSession.getAttribute("myID"));
		return "index";
	}

	@RequestMapping ("/login")
	public String login(){
		return "login";
	}

	@RequestMapping("/login-error") //щоб користувач потрапив на 404-page
	public String loginError(Model model) {
		model.addAttribute("login-error", true);
		return "login";
	}
}