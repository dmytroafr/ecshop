package com.echem.ecshop.service;


import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImp (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductDTO> getAll() {
        var products = productRepository.findAll();
        return mapper.fromProductList(products);
    }
    @Override
    public ProductDTO getProductById(Long id) {
        Product referenceById = productRepository.getReferenceById(id);
        return mapper.fromProduct(referenceById);
    }
    @Override
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
}
