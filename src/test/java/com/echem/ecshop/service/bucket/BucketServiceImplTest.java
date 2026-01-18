package com.echem.ecshop.service.bucket;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BucketServiceImplTest {

    @Mock
    private BucketRepository bucketRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BucketServiceImpl bucketService;

    private Bucket testBucket;
    private Product testProduct;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));

        testBucket = new Bucket();
        testBucket.setId(1L);
        testBucket.setUser(testUser);
        testBucket.setProductList(new ArrayList<>());
    }

    @Test
    void testAddBucketDetails_Success() {
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.addBucketDetails(1L, 1L);

        assertEquals(1, testBucket.getProductList().size());
        assertTrue(testBucket.getProductList().contains(testProduct));
        verify(bucketRepository).save(testBucket);
    }

    @Test
    void testCreateBucket_Success() {
        Bucket newBucket = new Bucket();
        newBucket.setId(2L);
        newBucket.setUser(testUser);

        when(bucketRepository.save(any(Bucket.class))).thenReturn(newBucket);

        bucketService.createBucket(testUser);

        verify(bucketRepository).save(any(Bucket.class));
    }

    @Test
    void testGetBucketDtoByUserId_Success() {
        testBucket.getProductList().add(testProduct);
        testBucket.getProductList().add(testProduct);

        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));

        BucketDTO result = bucketService.getBucketDtoByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getProductList().size());
        assertTrue(result.getSum() > 0);
    }

    @Test
    void testGetBucketDtoByUserId_EmptyBucket() {
        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));

        BucketDTO result = bucketService.getBucketDtoByUserId(1L);

        assertNotNull(result);
        assertEquals(0, result.getProductList().size());
        assertEquals(0, result.getSum());
    }

    @Test
    void testGetBucketDtoByUserId_NotFound() {
        when(bucketRepository.findByIdWithProducts(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bucketService.getBucketDtoByUserId(999L);
        });
    }

    @Test
    void testDeleteProductFromBucket_Success() {
        testBucket.getProductList().add(testProduct);

        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.deleteProductFromBucket(1L, 1L);

        assertEquals(0, testBucket.getProductList().size());
        verify(bucketRepository).save(testBucket);
    }

    @Test
    void testIncreaseProductAmount_Success() {
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.increaseProductAmount(1L, 1L);

        assertEquals(1, testBucket.getProductList().size());
        verify(bucketRepository).save(testBucket);
    }

    @Test
    void testDecreaseProductAmount_Success() {
        testBucket.getProductList().add(testProduct);
        testBucket.getProductList().add(testProduct);

        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.decreaseProductAmount(1L, 1L);

        assertEquals(1, testBucket.getProductList().size());
        verify(bucketRepository).save(testBucket);
    }

    @Test
    void testClearBucket_Success() {
        testBucket.getProductList().add(testProduct);
        testBucket.getProductList().add(testProduct);

        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.clearBucket(1L);

        assertEquals(0, testBucket.getProductList().size());
        verify(bucketRepository).save(testBucket);
    }

    @Test
    void testAddBucketDetails_MultipleSameProducts() {
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));
        when(bucketRepository.save(any(Bucket.class))).thenReturn(testBucket);

        bucketService.addBucketDetails(1L, 1L);
        bucketService.addBucketDetails(1L, 1L);

        assertEquals(2, testBucket.getProductList().size());
    }

    @Test
    void testGetBucketDtoByUserId_WithMultipleProducts() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(new BigDecimal("150.00"));

        testBucket.getProductList().add(testProduct);
        testBucket.getProductList().add(testProduct);
        testBucket.getProductList().add(product2);

        when(bucketRepository.findByIdWithProducts(1L)).thenReturn(Optional.of(testBucket));

        BucketDTO result = bucketService.getBucketDtoByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.getProductList().size()); // Two distinct products
        assertTrue(result.getSum() > 0);
    }
}
