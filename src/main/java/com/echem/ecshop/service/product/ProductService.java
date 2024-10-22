package com.echem.ecshop.service.product;


import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> getProductsByCategory(Pageable pageable, Long categoryId);

    Page<ProductDTO> getAllAvailableProductDTOs(Pageable pageable);
    ProductDTO getProductDtoById(Long productId);

    void addNewProduct(ProductDTO productDTO);
    void updateProduct(Long productId, ProductDTO productDTO);

    /**
     * Method for inner usage
     * @param productId
     * @return Product Entity
     */
    Product getProductRef (Long productId);

    /**
     * Method fo sitemap
     * @return List of ProductDTOs
     */
    List<ProductDTO> getAllAvailableProductDTOs();
}
