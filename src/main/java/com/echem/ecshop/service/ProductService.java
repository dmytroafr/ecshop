package com.echem.ecshop.service;

import com.echem.ecshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void addToUserBucket (Long productId, String username);
}
