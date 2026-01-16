package com.echem.ecshop.service.order;

import com.echem.ecshop.dao.OrderRepository;
import com.echem.ecshop.domain.*;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailDTO;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.OrderRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.bucket.BucketService;
import com.echem.ecshop.service.email.EmailService;
import com.echem.ecshop.service.product.ProductService;
import com.echem.ecshop.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BucketService bucketService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private UserDTO testUserDTO;
    private Product testProduct;
    private BucketDTO testBucketDTO;
    private OrderRequest testOrderRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testUserDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));

        BucketDetailDTO bucketDetail = new BucketDetailDTO(testProduct);
        bucketDetail.setAmount(new BigDecimal(2));
        bucketDetail.setSum(199.98);

        testBucketDTO = new BucketDTO();
        testBucketDTO.setId(1L);
        testBucketDTO.setProductList(new ArrayList<>(List.of(bucketDetail)));
        testBucketDTO.aggregate();

        testOrderRequest = new OrderRequest();
        testOrderRequest.setDelivery("Нова Пошта");
        testOrderRequest.setPayment("Картка");
    }

    @Test
    void testMakeOrder_Success() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setStatus(OrderStatus.NEW);
        savedOrder.setDetails(new ArrayList<>());

        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        when(bucketService.getBucketDtoByUserId(1L)).thenReturn(testBucketDTO);
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        doNothing().when(emailService).send(anyString(), anyString(), anyString());
        doNothing().when(bucketService).clearBucket(1L);

        Order result = orderService.makeOrder(testOrderRequest, testUserDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
        verify(emailService, times(2)).send(anyString(), anyString(), anyString());
        verify(bucketService).clearBucket(1L);
    }

    @Test
    void testOrderInform() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(testUser);
        order.setStatus(OrderStatus.NEW);

        doNothing().when(emailService).send(anyString(), anyString(), anyString());

        orderService.orderInform(testUserDTO, order);

        verify(emailService, times(2)).send(anyString(), anyString(), anyString());
        verify(emailService).send(eq("test@example.com"), anyString(), eq("Ваше замовлення"));
        verify(emailService).send(eq("sales@e-chem.com.ua"), anyString(), eq("#1"));
    }

    @Test
    void testGetOrderById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(testUser);
        order.setStatus(OrderStatus.NEW);
        order.setSum(new BigDecimal("199.98"));
        order.setDetails(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            orderService.getOrderById(999L);
        });
    }

    @Test
    void testFindAll() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUser(testUser);
        order1.setStatus(OrderStatus.NEW);
        order1.setDetails(new ArrayList<>());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setUser(testUser);
        order2.setStatus(OrderStatus.CLOSED);
        order2.setDetails(new ArrayList<>());

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<OrderDTO> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void testMakeOrder_WithMultipleProducts() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(new BigDecimal("150.00"));

        BucketDetailDTO detail1 = new BucketDetailDTO(testProduct);
        detail1.setAmount(new BigDecimal(2));
        detail1.setSum(199.98);

        BucketDetailDTO detail2 = new BucketDetailDTO(product2);
        detail2.setAmount(new BigDecimal(1));
        detail2.setSum(150.00);

        testBucketDTO.setProductList(new ArrayList<>(List.of(detail1, detail2)));
        testBucketDTO.aggregate();

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        when(bucketService.getBucketDtoByUserId(1L)).thenReturn(testBucketDTO);
        when(productService.getProduct(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id.equals(1L) ? testProduct : product2;
        });
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        doNothing().when(emailService).send(anyString(), anyString(), anyString());
        doNothing().when(bucketService).clearBucket(1L);

        Order result = orderService.makeOrder(testOrderRequest, testUserDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testMakeOrder_SetsCorrectOrderStatus() {
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        when(bucketService.getBucketDtoByUserId(1L)).thenReturn(testBucketDTO);
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            assertEquals(OrderStatus.NEW, order.getStatus());
            return order;
        });

        orderService.makeOrder(testOrderRequest, testUserDTO);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testMakeOrder_SetsDeliveryAndPayment() {
        when(userService.getUserByUsername("testuser")).thenReturn(testUser);
        when(bucketService.getBucketDtoByUserId(1L)).thenReturn(testBucketDTO);
        when(productService.getProduct(1L)).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            assertEquals("Нова Пошта", order.getDelivery());
            assertEquals("Картка", order.getPayment());
            return order;
        });

        orderService.makeOrder(testOrderRequest, testUserDTO);

        verify(orderRepository).save(any(Order.class));
    }
}
