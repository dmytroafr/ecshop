package com.echem.ecshop.service.product;

import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImp productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setOptPrice(new BigDecimal("89.99"));
        testProduct.setProductDescription("Test Description");
        testProduct.setPhotoUrl("/images/test.jpg");
        testProduct.setProducer("Test Producer");
        testProduct.setCountryProducer("Ukraine");
        testProduct.setOnStock(OnStock.ON_STOCK);
        testProduct.setCategories(new ArrayList<>());
    }

    @Test
    void testGetProductDtoById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO result = productService.getProductDtoById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductDtoById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            productService.getProductDtoById(999L);
        });
    }

    @Test
    void testGetAllAvailableProductDTOs_WithPageable() {
        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAllAvailable(pageable)).thenReturn(productPage);

        Page<ProductDTO> result = productService.getAllAvailableProductDTOs(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getTitle());
        verify(productRepository).findAllAvailable(pageable);
    }

    @Test
    void testGetProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProduct_NullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProduct(null);
        });
    }

    @Test
    void testGetProduct_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProduct(-1L);
        });
    }

    @Test
    void testGetProduct_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            productService.getProduct(999L);
        });
    }

    @Test
    void testGetProductsByCategory() {
        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAllAvailableByCategory(pageable, 1L)).thenReturn(productPage);

        Page<ProductDTO> result = productService.getProductsByCategory(pageable, 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAllAvailableByCategory(pageable, 1L);
    }

    @Test
    void testGetAllAvailableProductDTOs_AsList() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setOnStock(OnStock.ON_STOCK);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setOnStock(OnStock.ABSENT);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setTitle("Product 3");
        product3.setOnStock(OnStock.ON_STOCK);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2, product3));

        List<ProductDTO> result = productService.getAllAvailableProductDTOs();

        assertNotNull(result);
        assertEquals(2, result.size()); // Only ON_STOCK products
        verify(productRepository).findAll();
    }

    @Test
    void testAddNewProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("New Product");
        productDTO.setPrice(new BigDecimal("199.99"));

        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.addNewProduct(productDTO);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Updated Product");
        productDTO.setPrice(new BigDecimal("199.99"));
        productDTO.setProductDescription("Updated Description");
        productDTO.setCountryProducer("Poland");
        productDTO.setPhotoUrl("/images/updated.jpg");
        productDTO.setOptPrice(new BigDecimal("179.99"));
        productDTO.setProducer("Updated Producer");
        productDTO.setOnStock("ON_STOCK");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateProduct(1L, productDTO);

        assertEquals("Updated Product", testProduct.getTitle());
        assertEquals(new BigDecimal("199.99"), testProduct.getPrice());
        assertEquals("Updated Description", testProduct.getProductDescription());
        verify(productRepository).save(testProduct);
    }

    @Test
    void testUpdateProduct_NullDTO() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1L, null);
        });
    }

    @Test
    void testUpdateProduct_NullId() {
        ProductDTO productDTO = new ProductDTO();

        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(null, productDTO);
        });
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Updated Product");

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            productService.updateProduct(999L, productDTO);
        });
    }

    @Test
    void testUpdateProduct_PartialUpdate() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Updated Title Only");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateProduct(1L, productDTO);

        assertEquals("Updated Title Only", testProduct.getTitle());
        assertEquals(new BigDecimal("99.99"), testProduct.getPrice()); // Unchanged
        verify(productRepository).save(testProduct);
    }

    @Test
    void testGetTopProducts_Success() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Popular Product 1");
        product1.setPrice(new BigDecimal("99.99"));
        product1.setOnStock(OnStock.ON_STOCK);
        product1.setOrderCount(100L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Popular Product 2");
        product2.setPrice(new BigDecimal("149.99"));
        product2.setOnStock(OnStock.ON_STOCK);
        product2.setOrderCount(50L);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setTitle("Popular Product 3");
        product3.setPrice(new BigDecimal("79.99"));
        product3.setOnStock(OnStock.ON_STOCK);
        product3.setOrderCount(30L);

        List<Product> topProducts = List.of(product1, product2, product3);
        when(productRepository.findTopByOrderCount(any(Pageable.class))).thenReturn(topProducts);

        List<ProductDTO> result = productService.getTopProducts(3);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Popular Product 1", result.get(0).getTitle());
        assertEquals("Popular Product 2", result.get(1).getTitle());
        assertEquals("Popular Product 3", result.get(2).getTitle());
        verify(productRepository).findTopByOrderCount(any(Pageable.class));
    }

    @Test
    void testGetTopProducts_LimitTen() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setTitle("Product " + i);
            product.setPrice(new BigDecimal("99.99"));
            product.setOnStock(OnStock.ON_STOCK);
            product.setOrderCount((long) (100 - i));
            products.add(product);
        }

        when(productRepository.findTopByOrderCount(any(Pageable.class)))
            .thenReturn(products.subList(0, 10));

        List<ProductDTO> result = productService.getTopProducts(10);

        assertNotNull(result);
        assertEquals(10, result.size());
        verify(productRepository).findTopByOrderCount(PageRequest.of(0, 10));
    }

    @Test
    void testGetTopProducts_EmptyResult() {
        when(productRepository.findTopByOrderCount(any(Pageable.class)))
            .thenReturn(new ArrayList<>());

        List<ProductDTO> result = productService.getTopProducts(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findTopByOrderCount(any(Pageable.class));
    }

    @Test
    void testGetTopProducts_OnlyAvailableProducts() {
        Product availableProduct = new Product();
        availableProduct.setId(1L);
        availableProduct.setTitle("Available Product");
        availableProduct.setPrice(new BigDecimal("99.99"));
        availableProduct.setOnStock(OnStock.ON_STOCK);
        availableProduct.setOrderCount(100L);

        when(productRepository.findTopByOrderCount(any(Pageable.class)))
            .thenReturn(List.of(availableProduct));

        List<ProductDTO> result = productService.getTopProducts(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Available Product", result.get(0).getTitle());
    }}