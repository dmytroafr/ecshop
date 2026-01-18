package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> getBucketById(Long bucketId);
    
    // JOIN FETCH для завантаження productList разом з Bucket (уникнення LazyInitializationException)
    @Query("SELECT b FROM Bucket b LEFT JOIN FETCH b.productList WHERE b.id = :bucketId")
    Optional<Bucket> findByIdWithProducts(@Param("bucketId") Long bucketId);
}
