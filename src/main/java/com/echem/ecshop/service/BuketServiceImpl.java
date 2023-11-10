package com.echem.ecshop.service;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuketServiceImpl implements BucketService{
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public BuketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }
// Переписати ці два методи, не розумію навіщо другий метод, якщо ми додаємо завжди товар по одному
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

//    public void deleteProducts (Bucket bucket, List<Long> productIds){
//        List <Product> products = bucket.getProducts();
//        List <Product> newProductList = products == null ? new ArrayList<>():new ArrayList<>(products);
//        newProductList.addAll(getCollectRefProductsById(productIds));
//        bucket.setProducts(newProductList);
//        bucketRepository.save(bucket);
//    }


    @Override
    public BucketDTO deleteProductFromBucket(BucketDTO bucketDTO, Long productId) {

        if (bucketDTO==null || bucketDTO.getBucketDetails()==null){
            throw new IllegalArgumentException("Invalid input: bucketDTO or bucketDetails is null");
        }

        List<BucketDetailDTO> updateDetails = new ArrayList<>(bucketDTO.getBucketDetails());

        updateDetails = updateDetails
                .stream()
                .filter(bucketDetailDTO -> !bucketDetailDTO.getProductId().equals(productId))
                .collect(Collectors.toList());

        if (!bucketRepository.existsById(bucketDTO.getId())){
            throw new IllegalArgumentException("Bucket didn't find in repository");
        }

        Bucket bucket = bucketRepository.getReferenceById(bucketDTO.getId());
        bucket.setProducts(
                bucketDTO
                        .getBucketDetails()
                        .stream()
                        .map(bucketDetailDTO -> productRepository.getReferenceById(bucketDTO.getId()))
                        .toList());


        return new BucketDTO(bucketDTO.getId(),updateDetails.size(),updateDetails.stream().mapToDouble(BucketDetailDTO::getSum).sum(),updateDetails);

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
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum()+Double.valueOf(product.getPrice().toString()));
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }
}
