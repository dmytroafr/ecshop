package com.echem.ecshop.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("99.99"));
        product.setOnStock(OnStock.ON_STOCK);
    }

    @Test
    void testIsAvailable_WhenOnStock() {
        product.setOnStock(OnStock.ON_STOCK);

        assertTrue(product.isAvailable());
    }

    @Test
    void testIsAvailable_WhenAbsent() {
        product.setOnStock(OnStock.ABSENT);

        assertFalse(product.isAvailable());
    }

    @Test
    void testIsAvailable_WhenWaiting() {
        product.setOnStock(OnStock.WAITING);

        assertFalse(product.isAvailable());
    }

    @Test
    void testProductCreation() {
        Product newProduct = new Product();
        newProduct.setId(2L);
        newProduct.setTitle("New Product");
        newProduct.setPrice(new BigDecimal("199.99"));
        newProduct.setOptPrice(new BigDecimal("179.99"));
        newProduct.setProductDescription("Test description");
        newProduct.setPhotoUrl("/images/test.jpg");
        newProduct.setProducer("Test Producer");
        newProduct.setCountryProducer("Ukraine");
        newProduct.setOnStock(OnStock.ON_STOCK);

        assertEquals(2L, newProduct.getId());
        assertEquals("New Product", newProduct.getTitle());
        assertEquals(new BigDecimal("199.99"), newProduct.getPrice());
        assertEquals(new BigDecimal("179.99"), newProduct.getOptPrice());
        assertEquals("Test description", newProduct.getProductDescription());
        assertEquals("/images/test.jpg", newProduct.getPhotoUrl());
        assertEquals("Test Producer", newProduct.getProducer());
        assertEquals("Ukraine", newProduct.getCountryProducer());
        assertEquals(OnStock.ON_STOCK, newProduct.getOnStock());
    }

    @Test
    void testAllArgsConstructor() {
        Product product = new Product(
                1L,
                "Test",
                new BigDecimal("100"),
                new BigDecimal("90"),
                "Description",
                "/photo.jpg",
                "Producer",
                "Country",
                OnStock.ON_STOCK,
                null
        );

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test", product.getTitle());
    }
}
