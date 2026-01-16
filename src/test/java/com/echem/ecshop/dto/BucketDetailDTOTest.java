package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BucketDetailDTOTest {

    private Product testProduct;
    private BucketDetailDTO bucketDetailDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setPhotoUrl("/images/test.jpg");

        bucketDetailDTO = new BucketDetailDTO(testProduct);
    }

    @Test
    void testConstructorFromProduct() {
        assertEquals(1L, bucketDetailDTO.getProductId());
        assertEquals("Test Product", bucketDetailDTO.getTitle());
        assertEquals(0, new BigDecimal("99.99").compareTo(bucketDetailDTO.getPrice()));
        assertEquals(0, new BigDecimal("1.0").compareTo(bucketDetailDTO.getAmount()));
        assertEquals(99.99, bucketDetailDTO.getSum(), 0.01);
    }

    @Test
    void testSetAmount() {
        bucketDetailDTO.setAmount(new BigDecimal(5));

        assertEquals(new BigDecimal(5), bucketDetailDTO.getAmount());
    }

    @Test
    void testSetSum() {
        bucketDetailDTO.setSum(499.95);

        assertEquals(499.95, bucketDetailDTO.getSum(), 0.01);
    }

    @Test
    void testMultipleProducts() {
        bucketDetailDTO.setAmount(new BigDecimal(3));
        bucketDetailDTO.setSum(299.97);

        assertEquals(new BigDecimal(3), bucketDetailDTO.getAmount());
        assertEquals(299.97, bucketDetailDTO.getSum(), 0.01);
    }

    @Test
    void testWithDifferentPrice() {
        Product expensiveProduct = new Product();
        expensiveProduct.setId(2L);
        expensiveProduct.setTitle("Expensive Product");
        expensiveProduct.setPrice(new BigDecimal("999.99"));

        BucketDetailDTO detailDTO = new BucketDetailDTO(expensiveProduct);

        assertEquals(new BigDecimal("999.99"), detailDTO.getPrice());
        assertEquals(999.99, detailDTO.getSum(), 0.01);
    }
}
