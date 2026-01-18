package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.product.ProductService;
import com.echem.ecshop.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class MainController {

    private final UserService userService;
    private final ProductService productService;

    public MainController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping({"","/"})
    public String index (HttpSession httpSession, Principal principal, Model model) {
        if (principal != null) {
            if (httpSession.getAttribute("user") == null) {
                UserDTO userDTO = userService.getUserDTOByUserName(principal.getName());
                httpSession.setAttribute("user", userDTO);
            }
        }
        
        // Додавання топ 10 товарів
        List<ProductDTO> topProducts = productService.getTopProducts(10);
        log.info("Displaying {} top products on index page", topProducts != null ? topProducts.size() : 0);
        model.addAttribute("topProducts", topProducts);
        
        return "index";
    }

    @GetMapping ("/login")
    public String login(){
        return "login";
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