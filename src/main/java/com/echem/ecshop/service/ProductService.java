package com.echem.ecshop.service;


import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.dto.ProductRecordDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    List<ProductRecordDTO> getAllRecordDto();
    ProductDTO getProductById(Long id);
}
