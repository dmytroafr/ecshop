package com.echem.ecshop.dao;

import com.echem.ecshop.domain.OnStock;
import com.echem.ecshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Query("select p from Product p left join fetch p.categories where p.onStock= :on_stock")
    Page<Product> findAllByOnStock(Pageable pageable, OnStock on_stock);

    @Query("select p from Product p left join fetch p.categories c where p.onStock= :on_stock and c.id= :groupId")
    Page<Product> findAllByGroup(Pageable pageable,@Param("on_stock") OnStock on_stock, @Param("groupId") Long id);

//    Page<Product> findAllByOnStock(Pageable pageable, OnStock on_stock);


}
