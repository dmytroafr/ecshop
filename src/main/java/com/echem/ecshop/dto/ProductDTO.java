package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;

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
    public List<Category> categories;
}
