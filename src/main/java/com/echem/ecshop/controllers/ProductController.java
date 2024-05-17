package com.echem.ecshop.controllers;

import com.echem.ecshop.service.BucketService;
import com.echem.ecshop.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final BucketService bucketService;
    public ProductController(ProductService productService,BucketService bucketService) {
        this.productService = productService;
        this.bucketService = bucketService;
    }
    @GetMapping
    public String findAllByPage(Model model,Pageable pageable){
        var page = productService.findAllByPage(pageable);
        model.addAttribute("products", page);
        return "products";
    }
//    @GetMapping("/{id}/bucket")
//    public String addBucket(@PathVariable Long id, Principal principal){
//        if (principal==null){
//            return "redirect:/login";
//        }
//        bucketService.addToUserBucket(id, principal.getName());
//        return "redirect:/products";
//    }
}
