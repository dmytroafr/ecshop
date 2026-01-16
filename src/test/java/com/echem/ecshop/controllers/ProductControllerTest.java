package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        testProductDTO = new ProductDTO();
        testProductDTO.setId(1L);
        testProductDTO.setTitle("Test Product");
        testProductDTO.setPrice(new BigDecimal("99.99"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Page<ProductDTO> productPage = new PageImpl<>(List.of(testProductDTO));

        when(productService.getAllAvailableProductDTOs(any(Pageable.class), anyString(), anyString()))
                .thenReturn(productPage);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/products"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getAllAvailableProductDTOs(any(Pageable.class), anyString(), anyString());
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductDtoById(1L)).thenReturn(testProductDTO);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product"))
                .andExpect(model().attributeExists("product"));

        verify(productService).getProductDtoById(1L);
    }
}
