package com.echem.ecshop.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDTO {

    private long id;

    private int amountProducts;
    private Double sum;
    @Singular
    private List<BucketDetailDTO> bucketDetails = new ArrayList<>();

    public void aggregate(){
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}