package com.echem.ecshop.service;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.BucketDetails;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailsDTO;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.BucketDetailsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BucketServiceImpl implements BucketService{
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    // TODO Замінити продукт репозиторій на сервіс
    private final ProductService productService;
    private final UserService userService;
    private final BucketDetailsMapper mapper = BucketDetailsMapper.MAPPER;


    @Override
//    @Transactional
    public Bucket createBucket(User user) {
        var bucket = new Bucket();
        bucket.setUser(user);
        user.getBucket().add(bucket);
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
    public void addProductDetails (Bucket bucket, List<BucketDetails> bucketDetails) {
        bucket.getBucketDetails().addAll(bucketDetails);
        bucketRepository.save(bucket);
    }


    @Override
    public BucketDTO deleteProductFromBucket(Long productId, String userName) {
        var user = userService.findByName(userName);
        List<Bucket> bucketList = user.getBucket();
        var current = bucketList.get(bucketList.size() - 1);

        var changed = current.getBucketDetails()
                .stream().filter(bucketDetails -> !Objects.equals(bucketDetails.productId(), productId))
                .toList();
        current.setBucketDetails(changed);

        List<BucketDetailsDTO> bucketDetailsDTOS = mapper.fromBucketDetailsList(changed);
        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setBucketDetailsDTOS(bucketDetailsDTOS);
        bucketDTO.setId(current.getId());

        return bucketDTO;
    }

    @Override
    public BucketDTO getBucketByUser (String name){
        var user =  userService.findByName(name);
        List<Bucket> bucketList = user.getBucket();
        var currentBucket = bucketList.get(bucketList.size() - 1);
        var bucketDetailsDTOS = mapper.fromBucketDetailsList(currentBucket.getBucketDetails());

        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setId(currentBucket.getId());
        bucketDTO.setBucketDetailsDTOS(bucketDetailsDTOS);
        bucketDTO.aggregate();
        return bucketDTO;
    }
    public void addToUserBucket(Long productId, String username) {

        var user = userService.findByName(username);

        List<Bucket> bucketList = user.getBucket();
        var current = bucketList.get(bucketList.size() - 1);
        var bucketDetails = current.getBucketDetails();

        ProductDTO productById = productService.getProductById(productId);

//
//        var user = userService.findByName(username);
//
//        Bucket bucket = user.getBucket().getLast();
//
//        if (bucket == null){
//            var newBucket = createBucket(user, Collections.singletonList(productId));
//            user.getBucket().add(newBucket);
//            userService.save(user);
//        } else {
//            addProductDetails(bucket, Collections.singletonList(productId));
//        }
    }
}
