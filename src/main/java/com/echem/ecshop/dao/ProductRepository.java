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

    // ВАЖЛИВО: Використання @EntityGraph з Pageable може викликати warning HHH90003004
    // Hibernate завантажує ВСІ дані в пам'ять, тому для великих таблиць розгляньте альтернативи:
    // 1. JOIN FETCH в @Query (але Hibernate викине exception з Pageable)
    // 2. @BatchSize на рівні entity
    // 3. Два запити: спочатку ID з LIMIT, потім завантаження з categories
    @EntityGraph(attributePaths = {"categories"})
    @Query("select p from Product p join p.categories c where p.onStock='ON_STOCK' and c.id= :categoryId")
    Page<Product> findAllAvailableByCategory(Pageable pageable, @Param("categoryId") Long categoryId);

    @EntityGraph(attributePaths = {"categories"})
    @Query("select p from Product p where p.onStock='ON_STOCK'")
    Page<Product> findAllAvailable(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"categories"})
    List<Product> findAll();
    
    // Без @EntityGraph для уникнення warning "HHH90003004: firstResult/maxResults specified with collection fetch"
    // Категорії не потрібні для топ products на головній сторінці
    @Query("select p from Product p where p.onStock='ON_STOCK' order by p.orderCount desc, p.id asc")
    List<Product> findTopByOrderCount(Pageable pageable);
}