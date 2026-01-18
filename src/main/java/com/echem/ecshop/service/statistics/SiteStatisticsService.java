package com.echem.ecshop.service.statistics;

import com.echem.ecshop.domain.SiteStatistics;

public interface SiteStatisticsService {
    void recordVisit();
    Long getTotalVisits();
    Long getDailyVisits();
    SiteStatistics getTodayStatistics();
}
