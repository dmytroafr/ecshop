package com.echem.ecshop.controllers;


import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.CurrencyService;
import com.echem.ecshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller
public class MainController {

    private final ProductService productService;
    private final CurrencyService currencyService;

    public MainController(ProductService productService,CurrencyService currencyService) {
        this.productService = productService;
        this.currencyService = currencyService;
    }

    @RequestMapping({"","/"})
    public String index (Model model, HttpSession httpSession){

        String apiUrl = "https://v6.exchangerate-api.com/v6/3f905d8e5983cbdbe2a6bc4e/latest/USD";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        model.addAttribute("currency", currencyService.getUAH((currencyService.toMap(response))));

        if (httpSession.getAttribute("myID") == null){
            String uuid = UUID.randomUUID().toString();
            httpSession.setAttribute("myID", uuid);
        }
        model.addAttribute("uuid", httpSession.getAttribute("myID"));
        return "index";
    }

    @RequestMapping ("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error";
    }
    @RequestMapping ("/conditions")
    public String conditions(){
        return "conditions";
    }

    @RequestMapping("/about")
    public String about(){
        return "about";
    }

    @RequestMapping("/contacts")
    public String contacts(){
        return "contacts";
    }
}