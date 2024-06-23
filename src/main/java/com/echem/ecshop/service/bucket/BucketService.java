package com.echem.ecshop.service.bucket;


import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.dto.BucketDTO;

public interface BucketService {

    BucketDTO getBucketDtoByUser(Long userId);
    void addBucketDetails(Long productId, Long userId);
    void deleteProductFromBucket(Long productId, Long userId);
    Bucket createBucket(Long userId);
    void increaseProductAmount(Long productId, Long userId);
    void decreaseProductAmount(Long productId, Long userId);

    void clearBucket(Long bucketId);
}
