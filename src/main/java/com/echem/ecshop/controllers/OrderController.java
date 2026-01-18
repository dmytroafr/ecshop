package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.OrderRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Role enum already includes prefix ROLE_, so check authority directly
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public String findAllOrders (Model model){
        List<OrderDTO> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String makeAnOrder (@ModelAttribute OrderRequest orderRequest, Principal principal, HttpSession httpSession) {
        log.info("Processing new order request");
        if (principal==null){
            log.warn("Unauthenticated user trying to make an order");
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        log.info("Creating order for user: {}, delivery: {}, payment: {}", userDTO.getEmail(), orderRequest.getDelivery(), orderRequest.getPayment());
        Order order = orderService.makeOrder(orderRequest, userDTO);
        Long orderId = order.getId();
        log.info("Order created successfully with ID: {}", orderId);

        return "redirect:/orders/" + orderId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}")
    public String successOrder(@PathVariable Long orderId, HttpSession httpSession, Model model) {
        log.info("Displaying order confirmation page for order ID: {}", orderId);
        
        UserDTO currentUser = (UserDTO) httpSession.getAttribute("user");
        if (currentUser == null) {
            log.warn("User session not found, redirecting to login");
            return "redirect:/login";
        }
        
        OrderDTO orderById = orderService.getOrderById(orderId);
        log.debug("Loaded order details for order: {}", orderId);
        
        // Отримуємо повний Order для перевірки власника
        Order fullOrder = orderService.getOrderEntityById(orderId);
        
        // Перевірка що користувач має доступ до цього замовлення
        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().equals("ADMIN");
        boolean isOwner = fullOrder.getUser().getId().equals(currentUser.getId());
        
        if (!isOwner && !isAdmin) {
            log.warn("Access denied: User {} attempted to access order {} (owner: {})", 
                currentUser.getId(), orderId, fullOrder.getUser().getId());
            throw new AccessDeniedException("Ви не маєте доступу до цього замовлення");
        }
        
        log.info("User {} successfully accessed order {}", currentUser.getId(), orderId);
        String massage = "Ваше замовлення було успішно оформлене, очікуйте на виконання. Дякуємо";
        model.addAttribute("massage", massage);
        model.addAttribute("order", orderById);
        return "result";
    }


}
