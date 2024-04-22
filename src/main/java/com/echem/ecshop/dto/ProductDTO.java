package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductDTO(Long id,
                         String title,
                         BigDecimal price,
                         BigDecimal optPrice,
                         String productDescription,
                         String photoUrl,
                         String producer,
                         String countryProducer,
                         String onStock,
                         List<Category> categories
){
}
