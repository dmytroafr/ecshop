package com.echem.ecshop.service;

import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import org.aspectj.weaver.IClassFileProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;

    public ProductServiceImp(ProductRepository productRepository, UserService userService, BucketService bucketService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.formProductList(productRepository.findAll());
    }

    @Override
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null){
            throw new  RuntimeException("Не знайден користувач з ім'ям "+username);
        }
        Bucket bucket = user.getBucket();
        if (bucket == null){
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }
}
