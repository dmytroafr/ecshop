package com.echem.ecshop.service;


import com.echem.ecshop.dao.ProductRepository;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImp (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductDTO> getAll() {
        List<Product> productList = productRepository.findAll();
        return mapper.fromProductList(productList);
    }
    @Override
    public ProductDTO getProductById(Long id) {
        Product referenceById = productRepository.getReferenceById(id);
        return mapper.fromProduct(referenceById);
    }

}
