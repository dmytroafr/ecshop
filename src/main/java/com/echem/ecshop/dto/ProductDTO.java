package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;
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
                         OnStock onStock,
                         List<Category> categories
){
}
