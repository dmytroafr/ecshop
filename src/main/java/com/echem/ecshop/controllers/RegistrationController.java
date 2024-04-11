package com.echem.ecshop.controllers;

import com.echem.ecshop.registration.RegistrationRequest;
import com.echem.ecshop.registration.RegistrationService;
import com.echem.ecshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping()
    public String createUser(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirmToken (@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }


}
