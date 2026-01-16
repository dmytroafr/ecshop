package com.echem.ecshop.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestTest {

    @Test
    void testSettersAndGetters() {
        OrderRequest request = new OrderRequest();
        
        request.setDelivery("Нова Пошта");
        request.setPayment("Картка");

        assertEquals("Нова Пошта", request.getDelivery());
        assertEquals("Картка", request.getPayment());
    }

    @Test
    void testDefaultConstructor() {
        OrderRequest request = new OrderRequest();

        assertNull(request.getDelivery());
        assertNull(request.getPayment());
    }

    @Test
    void testWithDifferentDeliveryMethods() {
        OrderRequest request = new OrderRequest();

        request.setDelivery("Укрпошта");
        assertEquals("Укрпошта", request.getDelivery());

        request.setDelivery("Самовивіз");
        assertEquals("Самовивіз", request.getDelivery());
    }

    @Test
    void testWithDifferentPaymentMethods() {
        OrderRequest request = new OrderRequest();

        request.setPayment("Готівка");
        assertEquals("Готівка", request.getPayment());

        request.setPayment("Безготівковий розрахунок");
        assertEquals("Безготівковий розрахунок", request.getPayment());
    }

    @Test
    void testBothFieldsSet() {
        OrderRequest request = new OrderRequest();
        request.setDelivery("Нова Пошта");
        request.setPayment("Картка");

        assertNotNull(request.getDelivery());
        assertNotNull(request.getPayment());
        assertEquals("Нова Пошта", request.getDelivery());
        assertEquals("Картка", request.getPayment());
    }
}
