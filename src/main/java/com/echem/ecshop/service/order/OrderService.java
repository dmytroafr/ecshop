package com.echem.ecshop.service.order;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;

public interface OrderService {
    Order createOrder (OrderDTO orderDTO, String userName);
    Order getOrderById (Long orderId);
}
