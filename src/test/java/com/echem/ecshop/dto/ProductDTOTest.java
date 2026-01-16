package com.echem.ecshop.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
    }

    @Test
    void testSettersAndGetters() {
        productDTO.setId(1L);
        productDTO.setTitle("Test Product");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setOptPrice(new BigDecimal("89.99"));
        productDTO.setProductDescription("Test Description");
        productDTO.setPhotoUrl("/images/test.jpg");
        productDTO.setProducer("Test Producer");
        productDTO.setCountryProducer("Ukraine");
        productDTO.setOnStock("ON_STOCK");

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getTitle());
        assertEquals(new BigDecimal("99.99"), productDTO.getPrice());
        assertEquals(new BigDecimal("89.99"), productDTO.getOptPrice());
        assertEquals("Test Description", productDTO.getProductDescription());
        assertEquals("/images/test.jpg", productDTO.getPhotoUrl());
        assertEquals("Test Producer", productDTO.getProducer());
        assertEquals("Ukraine", productDTO.getCountryProducer());
        assertEquals("ON_STOCK", productDTO.getOnStock());
    }

    @Test
    void testDefaultConstructor() {
        ProductDTO dto = new ProductDTO();

        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getPrice());
    }

    @Test
    void testWithNullValues() {
        productDTO.setTitle(null);
        productDTO.setPrice(null);
        productDTO.setProductDescription(null);

        assertNull(productDTO.getTitle());
        assertNull(productDTO.getPrice());
        assertNull(productDTO.getProductDescription());
    }

    @Test
    void testPriceCalculation() {
        productDTO.setPrice(new BigDecimal("100.00"));
        productDTO.setOptPrice(new BigDecimal("90.00"));

        BigDecimal discount = productDTO.getPrice().subtract(productDTO.getOptPrice());

        assertEquals(new BigDecimal("10.00"), discount);
    }

    @Test
    void testStockStatus() {
        productDTO.setOnStock("ON_STOCK");
        assertEquals("ON_STOCK", productDTO.getOnStock());

        productDTO.setOnStock("OUT_OF_STOCK");
        assertEquals("OUT_OF_STOCK", productDTO.getOnStock());

        productDTO.setOnStock("EXPECTED");
        assertEquals("EXPECTED", productDTO.getOnStock());
    }
}
