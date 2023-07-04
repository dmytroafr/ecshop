package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private List<Category> categories;
    private String productDescription;
    private String photoUrl;
    private String producer;
    private String countryProducer;

}
