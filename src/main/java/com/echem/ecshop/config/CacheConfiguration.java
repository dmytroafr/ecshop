package com.echem.ecshop.config;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final CacheManager cacheManager;
    private final ProductService productService;

    public CacheConfiguration(CacheManager cacheManager, ProductService productService) {
        this.cacheManager = cacheManager;
        this.productService = productService;
    }

    @PostConstruct
    public void preloadCache(){
        Cache productsCache = cacheManager.getCache("productsCache");
        List<ProductDTO> allAvailableProductDTOs = productService.getAllAvailableProductDTOs();
        for (ProductDTO dto: allAvailableProductDTOs){
            productsCache.put(dto.id, dto);
        }
    }




}
