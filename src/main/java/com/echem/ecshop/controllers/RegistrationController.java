package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.registration.RegistrationService;
import com.echem.ecshop.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final RegistrationService registrationService;

    public RegistrationController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String registration(@ModelAttribute RegistrationRequest registrationRequest){
        return "registration";
    }

    @PostMapping()
    public String createUser(RegistrationRequest registrationRequest) {



        String token = registrationService.register(registrationRequest);


        registrationService.confirmToken(token);

        return "index";
    }

}
