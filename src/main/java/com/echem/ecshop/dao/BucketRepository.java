package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> getBucketById(Long bucketId);
}
