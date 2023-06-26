package com.echem.ecshop.service;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BuketServiceImpl implements BucketService{
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;

    public BuketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        return null;
    }

    @Override
    public void addProducts(Bucket bucket, List<Long> productIds) {

    }
}
