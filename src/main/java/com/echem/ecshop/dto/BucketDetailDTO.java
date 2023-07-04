package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailDTO {
    private String title;
    private Long productId;
    private BigDecimal price;
    private BigDecimal amount;
    private Double sum;
    private String productDescription;
    private String photoUrl;
    private String producer;
    private String countryProducer;


    public BucketDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.amount = new BigDecimal(1.0);
        this.sum = Double.valueOf(product.getPrice().toString());
        this.productDescription = product.getProductDescription();
        this.photoUrl = product.getPhotoUrl();
        this.producer = product.getProducer();
        this.countryProducer = product.getCountryProducer();

    }


}
