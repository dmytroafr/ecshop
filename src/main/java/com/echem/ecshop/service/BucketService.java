package com.echem.ecshop.service;


import com.echem.ecshop.dto.BucketDTO;

public interface BucketService {

   //  метод для контролеру
    BucketDTO getBucketByUser(String name);
    void addBucketDetails(Long productId, String username);
    BucketDTO deleteProductFromBucket(Long productId, String userName);
}
