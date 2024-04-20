package com.echem.ecshop.service;


import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.BucketDetails;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket createBucket (User user);
    void addProductDetails(Bucket bucket, List<BucketDetails> bucketDetails);
    BucketDTO getBucketByUser(String name);
    BucketDTO deleteProductFromBucket(Long productId, String userName);
    void addToUserBucket(Long productId, String username);

}
