package com.echem.ecshop.service.product;


import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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

    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "productsCache", key = "#productId")
    @Override
    public ProductDTO getProductDtoById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new NoSuchElementException("Product with id=" + productId + " doesn't exists");
        }
        return mapper.fromProduct(optionalProduct.get());
    }

    @Override
    public Page<ProductDTO> getAllAvailableProductDTOs(Pageable pageable) {
        Page<Product> allAvailableProducts = productRepository.findAllAvailable(pageable);
        return allAvailableProducts.map(mapper::fromProduct);
    }

    @Override
    public void addNewProduct(ProductDTO productDTO) {
        Product newProduct = mapper.toProduct(productDTO);
        log.info("Mapped ProductDTO to Product");

        Product savedProduct = productRepository.save(newProduct);
        log.info("Product {} was saved by Id {}", savedProduct.getTitle(), savedProduct.getId());
    }

    @Override
    public Product getProduct(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID: " + productId);
        }

        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Pageable pageable, Long categoryId) {
        Page<Product> groupPage = productRepository.findAllAvailableByCategory(pageable, categoryId);
        return groupPage.map(mapper::fromProduct);
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
    public List<ProductDTO> getAllAvailableProductDTOs() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> onStock = allProducts.stream()
                .filter(Product::isAvailable)
                .toList();

        return mapper.fromProductList(onStock);
    }
}
