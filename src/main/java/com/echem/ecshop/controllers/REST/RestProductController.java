package com.echem.ecshop.controllers.REST;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
public class RestProductController {

    private final ProductService productService;

    public RestProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId){
        if (productId<=0){
            return ResponseEntity.badRequest().build();
        }
        ProductDTO productDto = productService.getProductDtoById(productId);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAvailableProducts(Pageable pageable){
        Page<ProductDTO> allAvailableProducts = productService.getAllAvailableProductDTOs(pageable);
        return ResponseEntity.ok(allAvailableProducts);
    }




}
