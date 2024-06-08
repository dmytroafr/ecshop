package com.echem.ecshop.service;


import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImp (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAll() {
        var products = productRepository.findAll();
        return mapper.fromProductList(products);
    }
    @Override
    public ProductDTO getProductDtoById(Long id) {
        Product product = productRepository.getReferenceById(id);
        return mapper.fromProduct(product);
    }

    public List<ProductDTO> getAllRecordDto() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(mapper::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAllByPage(Pageable pageable) {
        Page<Product> all = productRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        return all.map(mapper::fromProduct);
    }

    @Override
    public void addNewProduct(ProductDTO productDTO) {
        productRepository.save(mapper.toProduct(productDTO));
    }

    @Override
    public Page<ProductDTO> findAllOnOnStock(Pageable pageable) {
        Page<Product> allByOnStock = productRepository.findAllByOnStock(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        return allByOnStock.map(mapper::fromProduct);
    }

    @Override
    public void setNewPrice(Long productId, double price) {
        Product product = productRepository.getReferenceById(productId);
        product.setPrice(new BigDecimal(price));
        productRepository.save(product);
    }

    @Override
    public Product getProductRef(Long productId) {
        return productRepository.getReferenceById(productId);
    }

    @Override
    public Page<ProductDTO> getProductsByGroup(Long groupNumber, Pageable pageable) {
        Page<Product> allByGroup = productRepository.findAllByGroup(groupNumber, pageable);
        return allByGroup.map(mapper::fromProduct);
    }
}
