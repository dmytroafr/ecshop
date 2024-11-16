package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String findAllProducts(
            @RequestParam(required = false, defaultValue = "title") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") Long categoryId,
            Model model,
            Pageable pageable) {

        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDir), sortField));

        Page<ProductDTO> products;

        if (categoryId == 0 ){
            products = productService.getAllAvailableProductDTOs(pageRequest);
        } else {
            products = productService.getProductsByCategory(pageRequest, categoryId);
        }

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", products);
        return "fragments/content :: products";
    }

    @GetMapping("/{productId}")
    public String getProduct(@PathVariable("productId") Long productId, Model model){
        if (productId<=0){
            throw new IllegalArgumentException();
        }
        ProductDTO productDto = productService.getProductDtoById(productId);
        model.addAttribute("product", productDto);
        return "products/product";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public String addNewProduct(@ModelAttribute("productDTO") ProductDTO productDTO){
        productService.addNewProduct(productDTO);
        return "redirect:/products";
    }
}
