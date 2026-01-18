package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.registration.RegistrationService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String registration(Model model){
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        return "registration";
    }

    @PostMapping()
    public String createUser(@ModelAttribute RegistrationRequest registrationRequest, 
                            RedirectAttributes redirectAttributes, 
                            Model model) {
        log.info("New user registration request for email: {}", registrationRequest.email());
        try {
            String token = registrationService.register(registrationRequest);
            registrationService.confirmToken(token);
            log.info("User registration completed successfully for: {}", registrationRequest.email());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Вітаємо! Реєстрація пройшла успішно. Ви можете увійти в свій акаунт.");
            return "redirect:/";
        } catch (IllegalStateException e) {
            log.error("Registration failed: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
            return "registration";
        }
    }

}
