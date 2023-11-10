package com.echem.ecshop.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDTO {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getAmountProducts() {
        return amountProducts;
    }

    public void setAmountProducts(int amountProducts) {
        this.amountProducts = amountProducts;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public List<BucketDetailDTO> getBucketDetails() {
        return bucketDetails;
    }

    public void setBucketDetails(List<BucketDetailDTO> bucketDetails) {
        this.bucketDetails = bucketDetails;
    }
}