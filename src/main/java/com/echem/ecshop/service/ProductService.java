package com.echem.ecshop.service;


import com.echem.ecshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    ProductDTO getProductById(Long id);
}
