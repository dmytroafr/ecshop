package com.echem.ecshop.dao;

import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    void deleteBucketByUser(User user);
}
