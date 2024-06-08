package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.ProductService;
import com.echem.ecshop.service.SessionObjectHolder;
import com.echem.ecshop.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final SessionObjectHolder sessionObjectHolder;

    public ProductController(ProductService productService, UserService userService, SessionObjectHolder sessionObjectHolder) {
        this.productService = productService;
        this.userService = userService;
        this.sessionObjectHolder = sessionObjectHolder;
    }

    @GetMapping
    public String findAllOnStockByPage(Model model,Pageable pageable){
        sessionObjectHolder.addClicks();
        Page<ProductDTO> allOnOnStock = productService.findAllOnOnStock(pageable);
        return getString(model, allOnOnStock);
    }

    private String getString(Model model, Page<ProductDTO> allOnOnStock) {
        model.addAttribute("products", allOnOnStock);

        int totalPages = allOnOnStock.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "products";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/create")
    public String sendProductDto (Model model, Principal principal){

        // TODO: не треба цю формочку передавати
        sessionObjectHolder.addClicks();

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
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public String addNewProduct(@ModelAttribute("productDTO") ProductDTO productDTO, Model model, Pageable pageable){
        productService.addNewProduct(productDTO);
        model.addAttribute("products", productService.findAllOnOnStock(pageable));
        return "products";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @GetMapping("/group")
    public String getPageableByGroup(@RequestParam("categoryId") Long categoryId, Model model, Pageable pageable){

        Page<ProductDTO> productDTOPage = productService.getProductsByGroup(categoryId, pageable);
        return getString(model, productDTOPage);
    }

}
