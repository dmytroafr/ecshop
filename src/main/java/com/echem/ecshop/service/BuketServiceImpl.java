package com.echem.ecshop.service;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuketServiceImpl implements BucketService{
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List <Product> productList = getCollectRefProductsById(productIds);

        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }
    public List<Product> getCollectRefProductsById(List<Long> productIds){
        return productIds.stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addProducts(Bucket bucket, List<Long> productIds) {
        List <Product> products = bucket.getProducts();
        List <Product> newProductList = products == null ? new ArrayList<>():new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsById(productIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }


    @Override
    public BucketDTO deleteProductFromBucket(Long productId, String username) {
       User user = userService.findByName(username);
       if (user==null){
           throw new RuntimeException("In method deleteProductFromBucket we didn't find user - " + username);
       }
        Bucket bucket = user.getBucket();
       if (bucket==null){
           throw new RuntimeException("In method deleteProductFromBucket bucket is null");
       } else {
           List<Product> currentProducts = bucket.getProducts();
           List<Product> newProductList = new ArrayList<>(currentProducts);
           newProductList.remove(
                   currentProducts
                           .stream()
                           .filter(product -> Objects.equals(product.getId(), productId))
                           .findAny()
                           .orElseThrow());
           bucket.setProducts(newProductList);
           bucketRepository.save(bucket);
       }
       return getBucketByUser(username);
    }

    @Override
    public BucketDTO getBucketByUser (String name){
        User user =  userService.findByName(name);
        if (user==null || user.getBucket()==null){
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getBucket().getProducts();

        for (Product product: products){
            BucketDetailDTO detail = mapByProductId.get(product.getId());
            if (detail==null){
                mapByProductId.put(product.getId(),new BucketDetailDTO(product));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal("1.0")));
                detail.setSum(detail.getSum()+Double.parseDouble(product.getPrice().toString()));
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }
    public void addToUserBucket(Long productId, String username) {
//        User user = userService.findByName(username);
//        if (user == null){
//            throw new  RuntimeException("Не знайден користувач з ім'ям "+username);
//        }
//        Bucket bucket = user.getBucket();
//        if (bucket == null){
//            Bucket newBucket = createBucket(user, Collections.singletonList(productId));
//            user.setBucket(newBucket);
//            userService.save(user);
//        } else {
//            addProducts(bucket, Collections.singletonList(productId));
//        }
    }
}
