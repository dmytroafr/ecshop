package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"categories"})
    @Query("select p from Product p join p.categories c where p.onStock='ON_STOCK' and c.id= :categoryId")
    Page<Product> findAllAvailableByCategory(Pageable pageable, @Param("categoryId") Long categoryId);

    @EntityGraph(attributePaths = {"categories"})
    @Query("select p from Product p where p.onStock='ON_STOCK'")
    Page<Product> findAllAvailable(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"categories"})
    List<Product> findAll();
}