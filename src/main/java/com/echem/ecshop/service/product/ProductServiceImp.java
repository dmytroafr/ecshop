package com.echem.ecshop.service.product;


import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImp (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductDTO> findAllByPage(Pageable pageable) {
        Page<Product> productPage = productRepository
                .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        log.info("Page {} from {}, total elements {} ALL",
                pageable.getPageNumber(), productPage.getTotalPages(), productPage.getTotalElements());

        Page<ProductDTO> map = productPage.map(mapper::fromProduct);
        log.info("Mapped Page<Product> to Page<ProductDTO> ALL");

        return map;
    }

    @Override
    public void addNewProduct(ProductDTO productDTO) {
        Product newProduct = mapper.toProduct(productDTO);
        log.info("Mapped ProductDTO to Product");

        Product savedProduct = productRepository.save(newProduct);
        log.info("Product {} was saved by Id {}", savedProduct.getTitle(), savedProduct.getId());
    }

    @Cacheable(cacheNames = "products")
    @Override
    public Page<ProductDTO> findAllOnOnStock(Pageable pageable) {
        Page<Product> productPage = productRepository
                .findAllByOnStock(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        log.info("Page {} from {}, total elements {} on STOCK",
                pageable.getPageNumber(), productPage.getTotalPages(), productPage.getTotalElements());

        Page<ProductDTO> map = productPage.map(mapper::fromProduct);
        log.info("Mapped Page<Product> to Page<ProductDTO> on STOCK");

        return map;
    }

    @Override
    public void setNewPrice(Long productId, double price) {
        Product product = productRepository.getReferenceById(productId);

        product.setPrice(new BigDecimal(price));
        productRepository.save(product);
    }

    @Override
    public Product getProductRef(Long productId) {
        Product referenceById = productRepository.getReferenceById(productId);
        log.info("Found product ref {}", referenceById);
        return referenceById;
    }

    @Cacheable("groups")
    @Override
    public Page<ProductDTO> getProductsByGroup(Long groupNumber, Pageable pageable) {
        Page<Product> groupPage = productRepository.findAllByGroup(groupNumber, pageable);
        log.info("Page {} from {}, total elements {} by GROUP {}",
                pageable.getPageNumber(), groupPage.getTotalPages(), groupPage.getTotalElements(), groupNumber);

        Page<ProductDTO> map = groupPage.map(mapper::fromProduct);
        log.info("Mapped Page<Product> to Page<ProductDTO> by GROUP {}", groupNumber);

        return map;
    }
}
