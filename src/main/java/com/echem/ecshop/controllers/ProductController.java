package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.ProductService;
import com.echem.ecshop.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String findAllOnStockByPage(Model model,Pageable pageable){
        Page<ProductDTO> allOnOnStock = productService.findAllOnOnStock(pageable);
        model.addAttribute("products", allOnOnStock);
        return "products";
    }

    @GetMapping("/create")
    public String sendProductDto (Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }

        String name = principal.getName();
        User byName = userService.findByName(name);
        if (!byName.getRole().equals(Role.ADMIN)){
            return "redirect:/login";
        }
        model.addAttribute("productDTO", new ProductDTO());
        return "addProducts";
    }

    @PostMapping("/create")
    public String addNewProduct(@ModelAttribute("productDTO") ProductDTO productDTO, Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
        String name = principal.getName();
        User byName = userService.findByName(name);
        if (!byName.getRole().equals(Role.ADMIN)){
            return "redirect:/login";
        }
        productService.addNewProduct(productDTO);
        return "products";
    }


    @GetMapping("/{productId}")
    public String setPrice (@PathVariable Long productId, @RequestParam(name = "price") double price,Principal principal){
        if (principal==null){
            return "redirect:/login";
        }
        String name = principal.getName();
        User byName = userService.findByName(name);
        if (!byName.getRole().equals(Role.ADMIN)){
            return "redirect:/login";
        }

        productService.setNewPrice(productId, price);

        return "products";

    }



}
