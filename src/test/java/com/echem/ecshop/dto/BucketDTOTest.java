package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BucketDTOTest {

    private BucketDTO bucketDTO;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        bucketDTO = new BucketDTO();
        bucketDTO.setId(1L);
        bucketDTO.setProductList(new ArrayList<>());

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setPhotoUrl("/images/test.jpg");
    }

    @Test
    void testAggregate_EmptyBucket() {
        bucketDTO.aggregate();

        assertEquals(0, bucketDTO.getSum());
        assertEquals(0, bucketDTO.getAmountProducts());
    }

    @Test
    void testAggregate_WithOneProduct() {
        BucketDetailDTO detail = new BucketDetailDTO(testProduct);
        detail.setAmount(new BigDecimal(1));
        detail.setSum(99.99);

        bucketDTO.getProductList().add(detail);
        bucketDTO.aggregate();

        assertEquals(99.99, bucketDTO.getSum(), 0.01);
        assertEquals(1, bucketDTO.getAmountProducts());
    }

    @Test
    void testAggregate_WithMultipleProducts() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(new BigDecimal("150.00"));

        BucketDetailDTO detail1 = new BucketDetailDTO(testProduct);
        detail1.setAmount(new BigDecimal(2));
        detail1.setSum(199.98);

        BucketDetailDTO detail2 = new BucketDetailDTO(product2);
        detail2.setAmount(new BigDecimal(3));
        detail2.setSum(450.00);

        bucketDTO.getProductList().add(detail1);
        bucketDTO.getProductList().add(detail2);
        bucketDTO.aggregate();

        assertEquals(649.98, bucketDTO.getSum(), 0.01);
        assertEquals(2, bucketDTO.getAmountProducts()); // 2 різних продукти в списку
    }

    @Test
    void testSettersAndGetters() {
        bucketDTO.setId(5L);
        bucketDTO.setSum(500.0);
        bucketDTO.setAmountProducts(10);

        assertEquals(5L, bucketDTO.getId());
        assertEquals(500.0, bucketDTO.getSum());
        assertEquals(10, bucketDTO.getAmountProducts());
    }

    @Test
    void testAddProductToBucket() {
        BucketDetailDTO detail = new BucketDetailDTO(testProduct);
        List<BucketDetailDTO> products = new ArrayList<>();
        products.add(detail);

        bucketDTO.setProductList(products);

        assertEquals(1, bucketDTO.getProductList().size());
        assertEquals("Test Product", bucketDTO.getProductList().get(0).getTitle());
    }
}
