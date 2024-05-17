package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public String userList (Model model){
//        model.addAttribute("users", userService.getAllUsers());
//        return "userList";
//    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal){

        if (principal==null) {
            throw new IllegalStateException("Але ж Ви не авторизовані");
        }
        User user = userService.findByEmail(principal.getName());
        UserDTO dto = UserDTO.builder()
                .username(user.getFirstName())
                .email(user.getEmail())
                .build();
        model.addAttribute("user",dto);
        return "profile";
    }
}