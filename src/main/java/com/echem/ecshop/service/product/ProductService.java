package com.echem.ecshop.service.product;


import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product getProductRef (Long id);

//    Page<ProductDTO> findAllOnOnStock(Pageable pageable, OnStock onStock);
//    Page<ProductDTO> findAllCategoriesByStock(Pageable pageable, OnStock onStock);

    Page<ProductDTO> getProductsByGroupAndStock(Long categoryId, Pageable pageable, OnStock onStock);
    Page<ProductDTO> findAll(Pageable pageable);

    ProductDTO getProductDto(Long productId);
    List<ProductDTO> getAllProducts();

    void addNewProduct(ProductDTO productDTO);
    void updateProduct(Long productId, ProductDTO productDTO);


    List<Category> getAllCategories();


}
