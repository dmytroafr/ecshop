package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public String makeAnOrder (@ModelAttribute OrderDTO orderDto, Principal principal, HttpSession httpSession) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        Order order = orderService.createOrder(orderDto, userDTO.id());
        Long orderId = order.getId();

        return "redirect:/order/" + orderId;
    }


    // TODO переробити на DTO

    @GetMapping("/{orderId}")
    public String successOrder (@PathVariable Long orderId, Model model){
        String massage = "Ваше замовлення було успішно оформлене, очікуйте на виконання. Дякуємо";
        model.addAttribute("massage",massage);
        Order orderById = orderService.getOrderById(orderId);
        model.addAttribute("order", orderById);
        return "result";
    }
}
