package com.echem.ecshop.controllers;

import com.echem.ecshop.service.statistics.SiteStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final SiteStatisticsService statisticsService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(Model model) {
        Long totalVisits = statisticsService.getTotalVisits();
        Long dailyVisits = statisticsService.getDailyVisits();
        
        model.addAttribute("totalVisits", totalVisits);
        model.addAttribute("dailyVisits", dailyVisits);
        
        return "admin";
    }
}
