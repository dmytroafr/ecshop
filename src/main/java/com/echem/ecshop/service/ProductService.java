package com.echem.ecshop.service;


import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDTO getProductDtoById(Long id);

    Product getProductRef (Long id);
    Page<ProductDTO> findAllByPage (Pageable pageable);
    Page<ProductDTO> findAllOnOnStock(Pageable pageable);

    void addNewProduct(ProductDTO productDTO);
    void setNewPrice(Long productId, double price);
}
