package com.echem.ecshop.controllers;
import com.echem.ecshop.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MainController {
	@RequestMapping({"","/"})
	public String index (){ return "index";}

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