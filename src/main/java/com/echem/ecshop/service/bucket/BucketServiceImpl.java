package com.echem.ecshop.service.bucket;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailDTO;
import com.echem.ecshop.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final ProductService productService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductService productService) {
        this.bucketRepository = bucketRepository;
        this.productService = productService;
    }

    @Override
    public void addBucketDetails(Long productId, Long userId) {
        log.debug("Adding bucket details for {}", productId);
        Product productRef = productService.getProductRef(productId);
        Bucket bucket = getBucketById(userId);
        bucket.addProduct(productRef);
        log.info("Add product {} to bucket {}", productId, bucket.getId());
        bucketRepository.save(bucket);
        log.info("Product {} were successfully added to Bucket {}",productId, bucket.getId());
    }
    @Override
    public void createBucket (User user){
        log.debug("Creating bucket method");
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        Bucket newBucket = bucketRepository.save(bucket);
        log.info("Created bucket {} for user {}", newBucket.getId(), user.getUsername());
    }

    private Bucket getBucketById(Long bucketId) {
        log.info("Getting bucket by bucketId {}", bucketId);
        return bucketRepository.getBucketById(bucketId)
                .orElseThrow(()-> {
                    log.error("Bucket by bucketId {} did not found", bucketId);
                    return new IllegalArgumentException(String.format("Bucket of user with bucketId %d not found", bucketId));
                }
        );
    }

    @Override
    public BucketDTO getBucketDtoByUserId(Long userId) {
        log.debug("Getting bucket DTO for {}", userId);

        Bucket bucket = getBucketById(userId);
        log.info("Found bucket {}, by userId ", bucket.getId());

        List<Product> inBucketProductList = bucket.getProductList();
        List<BucketDetailDTO> bucketDetails = summarizeBucketDetailsDTO(inBucketProductList);
        log.info("Mapped products into bucketDetailDto");

        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setId(bucket.getId());
        bucketDTO.setProductList(new ArrayList<>(bucketDetails));
        bucketDTO.aggregate();
        log.info("New BucketDTO was created and aggregated");

        return bucketDTO;
    }

    private static List<BucketDetailDTO> summarizeBucketDetailsDTO(List<Product> inBucketProductList) {
        log.debug("Summarizing bucket details for {} products private method", inBucketProductList.size());
        Map<Long, BucketDetailDTO> bucketDetailMap = new HashMap<>();
        if (inBucketProductList.isEmpty()){
            return Collections.emptyList();
        }
        for (Product product : inBucketProductList) {
            BucketDetailDTO bucketDetailDTO = bucketDetailMap.get(product.getId());
            if (bucketDetailDTO == null) {
                bucketDetailMap.put(product.getId(), new BucketDetailDTO(product));
            } else {
                bucketDetailDTO.setAmount(bucketDetailDTO.getAmount().add(new BigDecimal(1)));
                bucketDetailDTO.setSum(bucketDetailDTO.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }
        return bucketDetailMap.values().stream().toList();
    }


    @Override
    public void deleteProductFromBucket(Long productId, Long userId) {
        log.debug("Deleting product {} from bucket {} method", productId, userId);
        Bucket bucket = getBucketById(userId);
        bucket.removeProduct(productId);
        bucketRepository.save(bucket);
        log.info("Deleted product {} from the bucket", productId);
    }

    @Override
    public void increaseProductAmount(Long productId, Long userId) {
        addBucketDetails(productId, userId);
        log.info("Were increased amount of product {} in bucket {}", productId, userId);
    }

    @Override
    public void decreaseProductAmount(Long productId, Long userId) {
        Bucket bucket = getBucketById(userId);
        bucket.removeSingleProduct(productId);
        log.info("Were decreased amount of product {} from bucket {}", productId, bucket.getId());
        bucketRepository.save(bucket);
    }

    @Override
    public void clearBucket(Long id) {
        log.debug("Clearing bucket {}", id);
        Bucket bucket = getBucketById(id);
        bucket.getProductList().clear();
        bucketRepository.save(bucket);
        log.info("Bucket {} were cleared from products and saved", id);
    }
}