package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"","/"})
    public String index (HttpSession httpSession, Principal principal) {
        if (principal != null) {
            if (httpSession.getAttribute("user") == null) {
                UserDTO userDTO = userService.getUserDTOByUserName(principal.getName());
                httpSession.setAttribute("user", userDTO);
            }
        }
        return "index";
    }

    @GetMapping ("/login")
    public String login(){
        return "fragments/registration :: login";
    }

    @GetMapping ("/conditions")
    public String conditions(){
        return "fragments/content :: conditions";
    }

    @GetMapping("/contacts")
    public String contacts(){
        return "fragments/content :: contacts";
    }
}