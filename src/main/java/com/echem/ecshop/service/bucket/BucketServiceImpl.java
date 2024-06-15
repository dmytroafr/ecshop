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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BucketServiceImpl implements BucketService{

    private final BucketRepository bucketRepository;
    private final ProductService productService;
    private final UserService userService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductService productService, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void addBucketDetails(Long productId, String username) {
        User user = userService.findByName(username);
        Product productRef = productService.getProductRef(productId);

        Bucket bucket = user.getBucket();
        if (Objects.isNull(bucket)){
            Bucket newBucket = createBucket(user);
            newBucket.getProductList().add(productRef);
        } else {
            bucket.getProductList().add(productRef);
        }
        userService.save(user);
    }

    @Override
    public BucketDTO getBucketDtoByUser (String name){
        User user =  userService.findByName(name);

        if (user == null || user.getBucket() == null){
            return new BucketDTO();
        }

        Bucket bucket = user.getBucket();
        List<Product> productList = bucket.getProductList();
        Map<Long, BucketDetailDTO> detailDTOMap = new HashMap<>();

        for (Product product : productList){

            BucketDetailDTO bucketDetailDTO = detailDTOMap.get(product.getId());

            if (bucketDetailDTO == null){
                detailDTOMap.put(product.getId(), new BucketDetailDTO(product));
            } else {
                bucketDetailDTO.setAmount(bucketDetailDTO.getAmount().add(new BigDecimal(1)));
                bucketDetailDTO.setSum(bucketDetailDTO.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setProductList(new ArrayList<>(detailDTOMap.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }

    @Override
    public Bucket createBucket(User user) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        user.setBucket(bucket);
        return bucketRepository.save(bucket);
    }

    @Override
    public void deleteProductFromBucket(Long productId, String userName) {
        User user = userService.findByName(userName);

        List<Product> productList = user.getBucket().getProductList();

        productList.removeIf(product -> Objects.equals(product.getId(), productId));
        userService.save(user);
    }

    @Override
    public void increaseProductAmount(Long productId, String name) {
        addBucketDetails(productId,name);
    }

    @Override
    public void decreaseProductAmount(Long productId, String name) {
        User byUserName = userService.findByName(name);

        List<Product> productList = byUserName.getBucket().getProductList();

        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(productId)) {
                iterator.remove();
                break; // Видалити лише один екземпляр
            }
        }
        userService.save(byUserName);
    }

    @Transactional
    @Override
    public void deleteBucketByUser(User user){
        bucketRepository.deleteBucketByUser(user);
    }
}
