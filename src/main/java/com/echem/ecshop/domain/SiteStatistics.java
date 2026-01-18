package com.echem.ecshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "site_statistics")
public class SiteStatistics {
    private static final String SEQ_NAME = "site_statistics_seq";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    
    @Column(name = "visit_date", nullable = false, unique = true)
    private LocalDate visitDate;
    
    @Column(name = "daily_visits", nullable = false)
    private Long dailyVisits = 0L;
    
    @Column(name = "total_visits", nullable = false)
    private Long totalVisits = 0L;
}
