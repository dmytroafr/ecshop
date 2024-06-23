package com.echem.ecshop.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketDTO {
    public Long id;
    public int amountProducts;
    public Double sum;
    @Singular
    public List<BucketDetailDTO> productList = new ArrayList<>();

    public void aggregate(){
        this.amountProducts = productList.size();
        this.sum = productList.stream()
                .map(BucketDetailDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}