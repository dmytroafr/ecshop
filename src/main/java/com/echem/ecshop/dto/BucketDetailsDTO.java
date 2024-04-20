package com.echem.ecshop.dto;

import java.math.BigDecimal;
public record BucketDetailsDTO(Long id,
                               String title,
                               Long productId,
                               BigDecimal price,
                               BigDecimal optPrice,
                               BigDecimal amount,
                               Double sum,
                               String photoUrl,
                               String producer) {
}