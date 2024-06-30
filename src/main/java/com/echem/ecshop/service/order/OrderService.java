package com.echem.ecshop.service.order;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.OrderRequest;
import com.echem.ecshop.dto.UserDTO;

import java.util.List;

public interface OrderService {
    Order makeOrder(OrderRequest orderRequest, UserDTO userDTO);
    OrderDTO getOrderById (Long orderId);
    List<OrderDTO> findAll();
}
