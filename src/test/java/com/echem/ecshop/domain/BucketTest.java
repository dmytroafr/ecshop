package com.echem.ecshop.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BucketTest {

    private Bucket bucket;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        bucket = new Bucket();
        bucket.setId(1L);
        bucket.setProductList(new ArrayList<>());

        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setPrice(new BigDecimal("100.00"));

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(new BigDecimal("200.00"));
    }

    @Test
    void testAddProduct() {
        bucket.addProduct(product1);

        assertEquals(1, bucket.getProductList().size());
        assertTrue(bucket.getProductList().contains(product1));
    }

    @Test
    void testAddMultipleProducts() {
        bucket.addProduct(product1);
        bucket.addProduct(product2);
        bucket.addProduct(product1); // Add same product again

        assertEquals(3, bucket.getProductList().size());
    }

    @Test
    void testRemoveProduct() {
        bucket.addProduct(product1);
        bucket.addProduct(product1);
        bucket.addProduct(product2);

        bucket.removeProduct(1L);

        assertEquals(1, bucket.getProductList().size());
        assertFalse(bucket.getProductList().stream().anyMatch(p -> p.getId().equals(1L)));
        assertTrue(bucket.getProductList().stream().anyMatch(p -> p.getId().equals(2L)));
    }

    @Test
    void testRemoveSingleProduct() {
        bucket.addProduct(product1);
        bucket.addProduct(product1);
        bucket.addProduct(product2);

        bucket.removeSingleProduct(1L);

        assertEquals(2, bucket.getProductList().size());
        long count = bucket.getProductList().stream()
                .filter(p -> p.getId().equals(1L))
                .count();
        assertEquals(1, count);
    }

    @Test
    void testRemoveSingleProductRemovesOnlyOne() {
        bucket.addProduct(product1);
        bucket.addProduct(product1);

        bucket.removeSingleProduct(1L);

        assertEquals(1, bucket.getProductList().size());
    }

    @Test
    void testRemoveProductFromEmptyBucket() {
        bucket.removeProduct(1L);

        assertEquals(0, bucket.getProductList().size());
    }

    @Test
    void testRemoveSingleProductFromEmptyBucket() {
        bucket.removeSingleProduct(1L);

        assertEquals(0, bucket.getProductList().size());
    }

    @Test
    void testRemoveNonExistingProduct() {
        bucket.addProduct(product1);

        bucket.removeProduct(999L);

        assertEquals(1, bucket.getProductList().size());
    }
}
