package com.echem.ecshop.service.statistics;

import com.echem.ecshop.domain.SiteStatistics;
import com.echem.ecshop.repository.SiteStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteStatisticsServiceImpl implements SiteStatisticsService {
    
    private final SiteStatisticsRepository statisticsRepository;
    
    @Override
    @Transactional
    public void recordVisit() {
        LocalDate today = LocalDate.now();
        log.debug("Recording visit for date: {}", today);
        
        SiteStatistics stats = statisticsRepository.findByVisitDate(today)
                .orElseGet(() -> createNewDayStatistics(today));
        
        stats.setDailyVisits(stats.getDailyVisits() + 1);
        stats.setTotalVisits(stats.getTotalVisits() + 1);
        
        statisticsRepository.save(stats);
        log.debug("Visit recorded. Daily visits: {}, Total visits: {}", stats.getDailyVisits(), stats.getTotalVisits());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getTotalVisits() {
        Long total = statisticsRepository.getTotalVisitsAllTime();
        return total != null ? total : 0L;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getDailyVisits() {
        LocalDate today = LocalDate.now();
        return statisticsRepository.findByVisitDate(today)
                .map(SiteStatistics::getDailyVisits)
                .orElse(0L);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SiteStatistics getTodayStatistics() {
        LocalDate today = LocalDate.now();
        return statisticsRepository.findByVisitDate(today)
                .orElseGet(() -> createNewDayStatistics(today));
    }
    
    private SiteStatistics createNewDayStatistics(LocalDate date) {
        Long previousTotal = getTotalVisits();
        
        SiteStatistics newStats = new SiteStatistics();
        newStats.setVisitDate(date);
        newStats.setDailyVisits(0L);
        newStats.setTotalVisits(previousTotal);
        
        return statisticsRepository.save(newStats);
    }
}
