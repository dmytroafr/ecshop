package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.BucketService;
import com.echem.ecshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final BucketService bucketService;
    public ProductController(ProductService productService,BucketService bucketService) {
        this.productService = productService;
        this.bucketService = bucketService;
    }
    @GetMapping("")
    public String list (Model model){
        List<ProductDTO> list = productService.getAll();
        model.addAttribute("products", list);
        return "products";
    }
    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal){
        if (principal==null){
            return "redirect:/login";
        }
        bucketService.addToUserBucket(id, principal.getName());
        return "redirect:/products";
    }
}
