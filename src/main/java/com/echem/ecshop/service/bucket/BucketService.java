package com.echem.ecshop.service.bucket;


import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;

public interface BucketService {

    BucketDTO getBucketDtoByUser(Long userId);
    void createBucket (User user);
    void addBucketDetails(Long productId, Long userId);
    void deleteProductFromBucket(Long productId, Long userId);
    void increaseProductAmount(Long productId, Long userId);
    void decreaseProductAmount(Long productId, Long userId);
    void clearBucket(Long bucketId);
}
