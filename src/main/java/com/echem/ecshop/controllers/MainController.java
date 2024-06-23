package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.SessionObjectHolder;
import com.echem.ecshop.service.currency.CurrencyService;
import com.echem.ecshop.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.concurrent.ExecutionException;

@Controller
public class MainController {

    private final CurrencyService currencyService;
    private final SessionObjectHolder sessionObjectHolder;
    private final UserService userService;
    public MainController(CurrencyService currencyService, SessionObjectHolder sessionObjectHolder, UserService userService) {
        this.currencyService = currencyService;
        this.sessionObjectHolder = sessionObjectHolder;
        this.userService = userService;
    }

    @GetMapping({"","/"})
    public String index (Model model, HttpSession httpSession, Principal principal) throws ExecutionException, InterruptedException {

        if (principal != null && httpSession.getAttribute("userId") == null) {
            UserDTO userDTO = userService.findByUserName(principal.getName());
            httpSession.setAttribute("user", userDTO);
        }

        String rate = currencyService.getRate("USD").get();
        model.addAttribute("currency", rate);

        return "index";
    }

    @GetMapping ("/login")
    public String login(){
        return "login";
    }

    @GetMapping ("/conditions")
    public String conditions(){
        return "conditions";
    }

    @GetMapping("/about")
    public String about(){
        sessionObjectHolder.addClicks();
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts(){
        sessionObjectHolder.addClicks();
        return "contacts";
    }
}