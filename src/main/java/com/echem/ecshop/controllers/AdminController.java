package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.OrderStatus;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import com.echem.ecshop.service.statistics.SiteStatisticsService;
import com.echem.ecshop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final SiteStatisticsService statisticsService;
    private final UserService userService;
    private final OrderService orderService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(Model model) {
        // Статистика відвідувань
        Long totalVisits = statisticsService.getTotalVisits();
        Long dailyVisits = statisticsService.getDailyVisits();
        
        // Користувачі та замовлення
        List<UserDTO> users = userService.getUsers();
        List<OrderDTO> orders = orderService.findAll();
        
        model.addAttribute("totalVisits", totalVisits);
        model.addAttribute("dailyVisits", dailyVisits);
        model.addAttribute("users", users);
        model.addAttribute("orders", orders);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalOrders", orders.size());
        
        return "admin";
    }
    
    @PostMapping("/order/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam OrderStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Статус замовлення #" + orderId + " успішно оновлено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Помилка при оновленні статусу: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
