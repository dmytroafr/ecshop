package com.echem.ecshop.repository;

import com.echem.ecshop.domain.SiteStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SiteStatisticsRepository extends JpaRepository<SiteStatistics, Long> {
    
    Optional<SiteStatistics> findByVisitDate(LocalDate date);
    
    @Query("SELECT SUM(s.totalVisits) FROM SiteStatistics s")
    Long getTotalVisitsAllTime();
    
    @Modifying
    @Query("UPDATE SiteStatistics s SET s.dailyVisits = s.dailyVisits + 1, s.totalVisits = s.totalVisits + 1 WHERE s.visitDate = :date")
    void incrementVisitsForDate(@Param("date") LocalDate date);
}
