package com.echem.ecshop.controllers.REST;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/products")
public class RestProductController {

    private final ProductService productService;

    public RestProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId){
        if (productId == null || productId <= 0){
            log.warn("Некоректний productId: {}", productId);
            return ResponseEntity.badRequest().build();
        }
        try {
            ProductDTO productDto = productService.getProductDtoById(productId);
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            log.error("Помилка при отриманні товару з id {}: {}", productId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAvailableProducts(Pageable pageable){
        Page<ProductDTO> allAvailableProducts = productService.getAllAvailableProductDTOs(pageable);
        return ResponseEntity.ok(allAvailableProducts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProductsDto(){
        List<ProductDTO> allAvailableProductDTOs = productService.getAllAvailableProductDTOs();
        return ResponseEntity.ok(allAvailableProductDTOs);
    }





}
