package com.echem.ecshop.controllers;

import com.echem.ecshop.service.currency.CurrencyService;
import com.echem.ecshop.service.SessionObjectHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class MainController {

    private final CurrencyService currencyService;
    private final SessionObjectHolder sessionObjectHolder;
    public MainController(CurrencyService currencyService, SessionObjectHolder sessionObjectHolder) {
        this.currencyService = currencyService;
        this.sessionObjectHolder = sessionObjectHolder;
    }

    @GetMapping({"","/"})
    public String index (Model model, HttpSession httpSession){
        sessionObjectHolder.addClicks();
        ident(httpSession);
        model.addAttribute("uuid", httpSession.getAttribute("myID"));
        String rate = currencyService.getUAH();
        model.addAttribute("currency", rate);

        return "index";
    }

    @GetMapping ("/login")
    public String login(HttpSession httpSession, Model model){
        sessionObjectHolder.addClicks();
        return "login";
    }

    @GetMapping ("/conditions")
    public String conditions(){

        sessionObjectHolder.addClicks();
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

    private void ident (HttpSession session){
        if (session.getAttribute("myID") == null){
            String uuid = UUID.randomUUID().toString();
            session.setAttribute("myID", uuid);
        }
    }
}