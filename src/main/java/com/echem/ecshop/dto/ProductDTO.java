package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    public Long id;
    public String title;
    public BigDecimal price;
    public BigDecimal optPrice;
    public String productDescription;
    public String photoUrl;
    public String producer;
    public String countryProducer;

    public String onStock;

    public List<Category> categories = new ArrayList<>();
}
