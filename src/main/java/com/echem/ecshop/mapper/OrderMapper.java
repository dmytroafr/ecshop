package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.Order;
import com.echem.ecshop.dto.OrderDTO;
import org.mapstruct.factory.Mappers;

public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);
    OrderDTO orderToOrderDTO(Order order);
}
