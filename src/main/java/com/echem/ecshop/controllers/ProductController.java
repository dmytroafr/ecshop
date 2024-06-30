package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String findAllOnStockByPage(Model model,Pageable pageable){
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
    public String sendProductDto (Principal principal, HttpSession httpSession){
        if (principal == null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (!userDTO.getRole().equals(Role.ADMIN)){
            return "redirect:/login";
        }
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
    public String setPrice (@PathVariable Long productId, @RequestParam(name = "price") double price, Principal principal, HttpSession httpSession){
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (!userDTO.getRole().equals(Role.ADMIN)){
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
