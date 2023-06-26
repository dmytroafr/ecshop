package com.echem.ecshop.service;

import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;

import java.util.List;

public interface BucketService {
    Bucket createBucket (User user, List<Long> productIds);
    void addProducts(Bucket bucket, List<Long> productIds);
}
