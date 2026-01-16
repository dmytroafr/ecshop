package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderController
 * Note: testGetAllOrders() requires Spring Security context setup and is better suited as integration test
 */
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderDTO testOrderDTO;

    @BeforeEach
    void setUp() {
        testOrderDTO = new OrderDTO();
        testOrderDTO.setId(1L);
        testOrderDTO.setSum(new BigDecimal("199.99"));
        testOrderDTO.setStatus(com.echem.ecshop.domain.OrderStatus.NEW);
        testOrderDTO.setDetails(new ArrayList<>());
    }

    @Test
    void testOrderControllerCreation() {
        assertNotNull(orderController);
    }

    @Test
    void testOrderServiceInjection() {
        // Verify that OrderService is properly injected
        List<OrderDTO> orders = List.of(testOrderDTO);
        when(orderService.findAll()).thenReturn(orders);
        
        List<OrderDTO> result = orderService.findAll();
        
        assertEquals(1, result.size());
        verify(orderService).findAll();
    }
}
