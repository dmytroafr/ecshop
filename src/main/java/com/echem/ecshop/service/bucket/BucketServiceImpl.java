package com.echem.ecshop.service.bucket;

import com.echem.ecshop.dao.BucketRepository;
import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.BucketDetailDTO;
import com.echem.ecshop.service.product.ProductService;
import com.echem.ecshop.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;

    private final ProductService productService;
    private final UserService userService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductService productService, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void addBucketDetails(Long productId, Long userId) {

        Product productRef = productService.getProductRef(productId);

        Bucket bucket = bucketRepository.getBucketByUser_Id(userId);

        if (Objects.isNull(bucket)) {
            Bucket newBucket = createBucket(userId);
            newBucket.getProductList().add(productRef);
        } else {
            bucket.getProductList().add(productRef);
        }
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketDtoByUser(Long userId) {

        Bucket bucket = bucketRepository.getBucketByUser_Id(userId);

        if (Objects.isNull(bucket)) {
            bucket = createBucket(userId);
        }

        List<Product> productList = bucket.getProductList();
        Map<Long, BucketDetailDTO> detailDTOMap = new HashMap<>();

        for (Product product : productList) {

            BucketDetailDTO bucketDetailDTO = detailDTOMap.get(product.getId());
            if (bucketDetailDTO == null) {
                detailDTOMap.put(product.getId(), new BucketDetailDTO(product));
            } else {
                bucketDetailDTO.setAmount(bucketDetailDTO.getAmount().add(new BigDecimal(1)));
                bucketDetailDTO.setSum(bucketDetailDTO.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setId(bucket.getId());
        bucketDTO.setProductList(new ArrayList<>(detailDTOMap.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }

    @Override
    public Bucket createBucket(Long userId) {
        User refById = userService.getRefById(userId);
        Bucket bucket = new Bucket();
        bucket.setUser(refById);
        return bucketRepository.save(bucket);
    }

    @Override
    public void deleteProductFromBucket(Long productId, Long userId) {

        Bucket bucketByUserId = bucketRepository.getBucketByUser_Id(userId);
        List<Product> productList = bucketByUserId.getProductList();

        productList.removeIf(product -> Objects.equals(product.getId(), productId));

    }

    @Override
    public void increaseProductAmount(Long productId, Long userId) {
        addBucketDetails(productId, userId);
    }

    @Override
    public void decreaseProductAmount(Long productId, Long userId) {

        Bucket bucket = bucketRepository.getBucketByUser_Id(userId);
        List<Product> productList = bucket.getProductList();

        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(productId)) {
                iterator.remove();
                break;
            }
        }
        bucketRepository.save(bucket);
    }

    @Override
    public void clearBucket(Long bucketId) {
        Bucket bucket = bucketRepository.getBucketById(bucketId);
        bucket.getProductList().clear();
        bucketRepository.save(bucket);
    }
}