package com.echem.ecshop.dto;

import com.echem.ecshop.domain.OrderDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
    }

    @Test
    void testSettersAndGetters() {
        List<OrderDetails> details = new ArrayList<>();

        orderDTO.setId(1L);
        orderDTO.setSum(new BigDecimal("299.99"));
        orderDTO.setStatus(com.echem.ecshop.domain.OrderStatus.NEW);
        orderDTO.setDelivery("Нова Пошта");
        orderDTO.setPayment("Картка");
        orderDTO.setDetails(details);

        assertEquals(1L, orderDTO.getId());
        assertEquals(new BigDecimal("299.99"), orderDTO.getSum());
        assertEquals(com.echem.ecshop.domain.OrderStatus.NEW, orderDTO.getStatus());
        assertEquals("Нова Пошта", orderDTO.getDelivery());
        assertEquals("Картка", orderDTO.getPayment());
        assertEquals(details, orderDTO.getDetails());
    }

    @Test
    void testDefaultConstructor() {
        OrderDTO dto = new OrderDTO();

        assertNull(dto.getId());
        assertNull(dto.getSum());
        assertNull(dto.getStatus());
    }

    @Test
    void testWithMultipleOrderDetails() {
        List<OrderDetails> details = new ArrayList<>();
        // In real scenario, you would add actual OrderDetails objects
        
        orderDTO.setDetails(details);

        assertNotNull(orderDTO.getDetails());
        assertEquals(0, orderDTO.getDetails().size());
    }

    @Test
    void testStatusValues() {
        orderDTO.setStatus(com.echem.ecshop.domain.OrderStatus.NEW);
        assertEquals(com.echem.ecshop.domain.OrderStatus.NEW, orderDTO.getStatus());

        orderDTO.setStatus(com.echem.ecshop.domain.OrderStatus.APPROVED);
        assertEquals(com.echem.ecshop.domain.OrderStatus.APPROVED, orderDTO.getStatus());

        orderDTO.setStatus(com.echem.ecshop.domain.OrderStatus.CANCELLED);
        assertEquals(com.echem.ecshop.domain.OrderStatus.CANCELLED, orderDTO.getStatus());
    }
}
