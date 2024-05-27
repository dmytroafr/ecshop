package com.echem.ecshop.service;


import com.echem.ecshop.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    List<ProductDTO> getAllRecordDto();
    ProductDTO getProductById(Long id);
    Page<ProductDTO> findAllByPage (Pageable pageable);
    void addNewProduct(ProductDTO productDTO);
    Page<ProductDTO> findAllOnOnStock(Pageable pageable);

    void setNewPrice(Long productId, double price);
}
