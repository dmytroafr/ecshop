package com.echem.ecshop.service.bucket;


import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;

public interface BucketService {

    BucketDTO getBucketDtoByUser(String name);
    void addBucketDetails(Long productId, String username);
    void deleteProductFromBucket(Long productId, String userName);
    Bucket createBucket(User user);
    void deleteBucketByUser(User user);
    void increaseProductAmount(Long productId, String name);
    void decreaseProductAmount(Long productId, String name);
}