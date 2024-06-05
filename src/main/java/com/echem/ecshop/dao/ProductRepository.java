package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p left join fetch p.categories where p.onStock='ON_STOCK'")
    Page<Product> findAllByOnStock(Pageable pageable);


}
