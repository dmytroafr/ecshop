package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.OrderRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyAuthority()")
    @PostMapping("/create")
    public String makeAnOrder (@ModelAttribute OrderRequest orderRequest, Principal principal, HttpSession httpSession) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        Order order = orderService.makeOrder(orderRequest, userDTO);
        Long orderId = order.getId();

        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/{orderId}")
    public String successOrder (@PathVariable Long orderId, Model model){
        String massage = "Ваше замовлення було успішно оформлене, очікуйте на виконання. Дякуємо";
        model.addAttribute("massage",massage);
        OrderDTO orderById = orderService.getOrderById(orderId);
        model.addAttribute("order", orderById);
        return "result";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String findAllOrders (Model model){
        List<OrderDTO> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }
}
