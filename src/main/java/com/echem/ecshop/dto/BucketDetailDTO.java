package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailDTO {
    private String title;
    private Long productId;
    private BigDecimal price;
    private BigDecimal optPrice;
    private BigDecimal amount;
    private Double sum;
    private String photoUrl;
    private String producer;
    private String countryProducer;

    @Enumerated(EnumType.STRING)
    private OnStock onStock;
    @Singular
    private List<Category> categories;
    public BucketDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.optPrice = product.getOptPrice();
        this.amount = new BigDecimal("1.0");
//        this.sum = Double.valueOf(product.getPrice().toString());
        this.sum = amount.multiply(price).doubleValue();
        this.photoUrl = product.getPhotoUrl();
        this.producer = product.getProducer();
        this.countryProducer = product.getCountryProducer();
        this.onStock = product.getOnStock();
        this.categories = product.getCategories();
    }
}