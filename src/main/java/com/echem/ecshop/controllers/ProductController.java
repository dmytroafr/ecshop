package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.OnStock;
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
            @RequestParam(required = false, defaultValue = "ON_STOCK") OnStock onStock,
            Model model,
            Pageable pageable) {

        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDir), sortField));

        log.info("Our pageable {}",pageRequest);
        Page<ProductDTO> products = productService.getProductsByGroupAndStock(
                categoryId, pageRequest,
                onStock);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", products);
        return "products/products";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit")
    public String findAllProductsAdmin(
            @RequestParam(required = false, defaultValue = "title") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            Model model,
            Pageable pageable) {

        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDir), sortField));

        Page<ProductDTO> products = productService.findAll(pageRequest);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("products", products);
        model.addAttribute("productDTO", new ProductDTO());
        return "products/editProducts";
    }

    @PatchMapping("/{productId}")
    public String updateProductList(@ModelAttribute("productDTO") ProductDTO productDTO,
                                    @PathVariable("productId") Long productId) {
        log.info("Trying to update product price by id");
        productService.updateProduct(productId, productDTO);
        return "redirect:/products/edit";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/create")
    public String sendProductDto (Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("allCategories", productService.getAllCategories());
        return "products/addProducts";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public String addNewProduct(@ModelAttribute("productDTO") ProductDTO productDTO){
        productService.addNewProduct(productDTO);
        return "redirect:/products";
    }
}
