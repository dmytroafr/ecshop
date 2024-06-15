package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.registration.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {this.registrationService = registrationService;}

    @GetMapping
    public String registration(){
        return "registration";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("registrationRequest") RegistrationRequest request, Model model) {
        String register = registrationService.register(request);
        model.addAttribute("token", register);
        String result = "Для закінчення реєстрації Вам відправлено листа на Ваш email," +
                "будь ласка, підтвердіть свій email перейшовши за посиланням";
        model.addAttribute("result", result);
        return "index";
    }

    @GetMapping("/confirm")
    public String confirmToken (@RequestParam("token") String token, Model model){
        String result = registrationService.confirmToken(token);
        model.addAttribute("result", result);
        return "index";
    }
}
