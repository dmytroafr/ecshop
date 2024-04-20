package com.echem.ecshop.domain;

import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
@Entity
@Builder
@Table(name = "bucket_details")
public record BucketDetails(
        @SequenceGenerator(name = "bucket_details_seq", sequenceName = "bucket_details_seq",allocationSize = 1)
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "bucket_details_seq")
        Long id,
        @ManyToOne
        @JoinColumn(name = "bucket_id")
        Bucket bucket,
        String title,
        Long productId,
        BigDecimal price,
        BigDecimal optPrice,
        BigDecimal amount,
        Double sum,
        String photoUrl,
        String producer) {
}
