package com.echem.ecshop.controllers;


import com.echem.ecshop.service.CurrencyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class MainController {
    private final CurrencyService currencyService;
    public MainController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping({"","/"})
    public String index (Model model, HttpSession httpSession){

        ident(httpSession);
        model.addAttribute("uuid", httpSession.getAttribute("myID"));

        String rate = currencyService.getPLN();
        model.addAttribute("currency", rate);

        return "index";
    }

    @GetMapping ("/login")
    public String login(HttpSession httpSession, Model model){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error";
    }
    @GetMapping ("/conditions")
    public String conditions(){
        return "conditions";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts(){
        return "contacts";
    }

    private void ident (HttpSession session){
        if (session.getAttribute("myID") == null){
            String uuid = UUID.randomUUID().toString();
            session.setAttribute("myID", uuid);
        }
    }
}