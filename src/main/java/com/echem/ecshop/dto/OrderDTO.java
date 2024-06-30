package com.echem.ecshop.dto;

import com.echem.ecshop.domain.OrderDetails;
import com.echem.ecshop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    public Long id;
    public LocalDateTime created;
    public LocalDateTime updated;
    public LocalDateTime closed;
    public BigDecimal sum;
    public List<OrderDetails> details;
    public OrderStatus status;
    public String delivery;
    public String payment;
}
