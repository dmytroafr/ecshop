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

    @PostMapping()
    public String createUser(@ModelAttribute("registrationRequest") RegistrationRequest request, Model model) {
        registrationService.register(request);
        model.addAttribute("registration", "waiting");
        return "index";
    }
    @GetMapping("/confirm")
    public String confirmToken (@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
