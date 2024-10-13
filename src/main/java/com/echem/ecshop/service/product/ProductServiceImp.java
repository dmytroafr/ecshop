package com.echem.ecshop.service.product;


import com.echem.ecshop.dao.CategoryRepository;
import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImp(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addNewProduct(ProductDTO productDTO) {
        Product newProduct = mapper.toProduct(productDTO);
        log.info("Mapped ProductDTO to Product");

        Product savedProduct = productRepository.save(newProduct);
        log.info("Product {} was saved by Id {}", savedProduct.getTitle(), savedProduct.getId());
    }

//    @Cacheable(cacheNames = "products")
//    @Override
//    public Page<ProductDTO> findAllOnOnStock(Pageable pageable, OnStock onStock) {
//        Page<Product> productPage = productRepository
//                .findAllByOnStock(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),onStock);
//        log.info("Page {} from {}, total elements {} on STOCK",
//                pageable.getPageNumber(), productPage.getTotalPages(), productPage.getTotalElements());
//
//        Page<ProductDTO> map = productPage.map(mapper::fromProduct);
//        log.info("Mapped Page<Product> to Page<ProductDTO> on STOCK");
//
//        return map;
//    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository
                .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        log.info("Page {} from {}, total elements {}",
                pageable.getPageNumber(), productPage.getTotalPages(), productPage.getTotalElements());

        Page<ProductDTO> map = productPage.map(mapper::fromProduct);
        log.info("Mapped Page<Product> to Page<ProductDTO>");
        return map;
    }

//    @Override
//    public void setNewPrice(Long productId, double price) {
//        log.debug("Setting new price for product {} setNewPrice() method", productId);
//        Product product = productRepository.getReferenceById(productId);
//        product.setPrice(new BigDecimal(price));
//        productRepository.save(product);
//        log.info("Price was changed");
//    }

    @Override
    public Product getProductRef(Long productId) {
        log.debug("Get productRef by id {}", productId);
        Product referenceById = productRepository.getReferenceById(productId);
        log.info("Found product ref {}", referenceById);
        return referenceById;
    }

    @Override
    public Page<ProductDTO> getProductsByGroupAndStock(Long categoryId, Pageable pageable, OnStock onStock) {

        Page<Product> groupPage;
        if (categoryId!=0){
            log.info("Get products by category id {}", categoryId);
            groupPage = productRepository.findAllByGroup(pageable, onStock, categoryId);
        } else {
            log.info("Get products by all categories");
            groupPage = productRepository.findAllByOnStock(pageable, onStock);
        }

        log.info("Page {} from {}, total elements {} by GROUP {}",
                pageable.getPageNumber(), groupPage.getTotalPages(), groupPage.getTotalElements(), categoryId);

        Page<ProductDTO> map = groupPage.map(mapper::fromProduct);
        log.info("Mapped Page<Product> to Page<ProductDTO> by GROUP {}", categoryId);

        return map;
    }

    @Override
    public List<Category> getAllCategories() {
        log.debug("Get all categories");
        return categoryRepository.findAll();
    }

    @Override
    public void updateProduct(Long productId, ProductDTO productDTO) {
        log.debug("Updating product");

        if (productDTO==null || productId==null){
            throw new IllegalArgumentException("ProductDTO or it's Id cannot be null");
        }

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));

        if (productDTO.getTitle()!=null){
            product.setTitle(productDTO.getTitle());
            log.info("Updated title {}", product.getTitle());
        }
        if (productDTO.getPrice()!=null){
            product.setPrice(productDTO.getPrice());
            log.info("Updated price {}", product.getPrice());
        }
        if (productDTO.getProductDescription()!=null){
            product.setProductDescription(productDTO.getProductDescription());
            log.info("Updated productDescription {}", product.getProductDescription());
        }
        if (productDTO.getCountryProducer()!=null){
            product.setCountryProducer(productDTO.getCountryProducer());
            log.info("Updated countryProducer {}", product.getCountryProducer());
        }
        if (productDTO.getPhotoUrl()!=null){
            product.setPhotoUrl(productDTO.getPhotoUrl());
            log.info("Updated photoUrl {}", product.getPhotoUrl());
        }
        if (productDTO.getOptPrice()!=null){
            product.setOptPrice(productDTO.getOptPrice());
            log.info("Updated optPrice {}", product.getOptPrice());
        }
        if (productDTO.getProducer()!=null){
            product.setProducer(productDTO.getProducer());
            log.info("Updated producer {}", product.getProducer());
        }
        if (productDTO.getOnStock()!=null){
            product.setOnStock(OnStock.valueOf(productDTO.getOnStock()));
            log.info("Updated onStock {}", product.getOnStock());
        }
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductDto(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new NoSuchElementException("Product with id=" + productId + " doesn't exists");
        }
        return mapper.fromProduct(optionalProduct.get());
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> all = productRepository.findAll();
        return mapper.fromProductList(all);
    }
}
