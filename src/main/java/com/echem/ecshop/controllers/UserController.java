package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String profileUser(@PathVariable String username, Model model){
        log.info("request to /users/{username}");
        Map<String, String> userMap = userService.getUserDetailsMap(username);
        model.addAttribute("user", userMap);
        log.info("UserMap was sent to view");
        return "users/profile";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String getAllUsers(Model model){
        log.info("request to /users");
        List<UserDTO> users = userService.getUsers();
        model.addAttribute("users", users);
        return "users/users";
    }
}