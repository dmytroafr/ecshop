package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.dto.ProductRecordDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductRecordDTO> findAllBy();
}
