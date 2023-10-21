package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal optPrice;
    private String productDescription;
    private String photoUrl;
    private String producer;
    private String countryProducer;
    @Enumerated(EnumType.STRING)
    private OnStock onStock;
    @Singular
    private List<Category> categories;
}
