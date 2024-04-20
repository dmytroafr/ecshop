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
    private Long id;
    private int amountProducts;
    private Double sum;
    @Singular
    private List<BucketDetailsDTO> bucketDetailsDTOS = new ArrayList<>();

    public void aggregate(){
        this.amountProducts = bucketDetailsDTOS.size();
        this.sum = bucketDetailsDTOS.stream()
                .map(BucketDetailsDTO::sum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}