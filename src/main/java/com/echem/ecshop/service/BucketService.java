package com.echem.ecshop.service;


import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket createBucket (User user, List<Long> productIds);
    void addProducts(Bucket bucket, List<Long> productIds);
    BucketDTO getBucketByUser(String name);
    BucketDTO deleteProductFromBucket(BucketDTO bucketDTO, Long id);

}
