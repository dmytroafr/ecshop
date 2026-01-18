package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ChangePasswordRequest;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import com.echem.ecshop.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String profileUser(@PathVariable String username, Model model, Principal principal){
        // Перевірка, що користувач переглядає свій профіль
        if (!username.equals(principal.getName())) {
            log.warn("User {} tried to access profile of {}", principal.getName(), username);
            return "redirect:/users/" + principal.getName();
        }
        
        Map<String, String> mappedUser = userService.getUserDetailsMap(username);
        model.addAttribute("user", mappedUser);
        model.addAttribute("username", username);
        return "users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}/change-password")
    public String showChangePasswordForm(@PathVariable String username, Model model, Principal principal) {
        // Перевірка, що користувач змінює свій пароль
        if (!username.equals(principal.getName())) {
            log.warn("User {} tried to change password of {}", principal.getName(), username);
            return "redirect:/users/" + principal.getName() + "/change-password";
        }
        
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        model.addAttribute("username", username);
        return "users/change-password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{username}/change-password")
    public String changePassword(
            @PathVariable String username,
            @Valid @ModelAttribute ChangePasswordRequest request,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        // Перевірка, що користувач змінює свій пароль
        if (!username.equals(principal.getName())) {
            log.warn("User {} tried to change password of {}", principal.getName(), username);
            return "redirect:/users/" + principal.getName() + "/change-password";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("username", username);
            return "users/change-password";
        }
        
        // Перевірка співпадіння нового пароля та підтвердження
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Новий пароль та підтвердження не співпадають");
            model.addAttribute("username", username);
            return "users/change-password";
        }
        
        try {
            userService.changePassword(username, request.getCurrentPassword(), request.getNewPassword());
            redirectAttributes.addFlashAttribute("success", "Пароль успішно змінено");
            log.info("Password changed successfully for user {}", username);
            return "redirect:/users/" + username;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            log.error("Failed to change password for user {}: {}", username, e.getMessage());
            return "users/change-password";
        }
    }

    // Role enum already includes prefix ROLE_, so check authority directly
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public String getAllUsers(Model model){
        log.info("request to /users");
        List<UserDTO> allUsers = userService.getUsers();
        model.addAttribute("users", allUsers);
        return "users/users";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}/orders")
    public String getUserOrders(@PathVariable String username, Model model, Principal principal) {
        // Перевірка, що користувач переглядає свої замовлення
        if (!username.equals(principal.getName())) {
            log.warn("User {} tried to access orders of {}", principal.getName(), username);
            return "redirect:/users/" + principal.getName() + "/orders";
        }
        
        List<OrderDTO> orders = orderService.findOrdersByUsername(username);
        model.addAttribute("orders", orders);
        model.addAttribute("username", username);
        return "users/orders";
    }
}