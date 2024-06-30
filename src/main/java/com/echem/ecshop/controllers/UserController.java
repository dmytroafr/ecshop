package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) throws Exception {
        log.info("request to /users/profile");
        if (principal==null) {
            log.error("Principal is null");
            throw new IllegalAccessException("User has not been logged in");
        }
        log.info("principal {} authenticated", principal.getName());
        Map<String, Object> userMap = userService.getUserDetailsMap(principal.getName());
        model.addAttribute("user", userMap);
        log.info("UserMap was sent to view");
        return "profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getAllUsers(Model model, Principal principal) throws Exception {
        log.info("request to /users");
        if (principal==null) {
            log.error("Principal is null");
            throw new IllegalAccessException("User has not been logged in");
        }
        log.info("principal {} authenticated", principal.getName());
        List<UserDTO> users = userService.getUsers();
        model.addAttribute("users", users);
        return "users";
    }
}