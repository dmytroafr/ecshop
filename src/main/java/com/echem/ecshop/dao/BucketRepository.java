package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository <Bucket, Long> {
}
