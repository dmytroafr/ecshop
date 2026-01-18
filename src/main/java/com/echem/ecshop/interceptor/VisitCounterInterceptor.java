package com.echem.ecshop.interceptor;

import com.echem.ecshop.service.statistics.SiteStatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitCounterInterceptor implements HandlerInterceptor {
    
    private final SiteStatisticsService statisticsService;
    private static final String VISIT_RECORDED = "visitRecorded";
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        HttpSession session = request.getSession();
        
        // Записуємо відвідування тільки один раз за сесію
        if (session.getAttribute(VISIT_RECORDED) == null) {
            try {
                statisticsService.recordVisit();
                session.setAttribute(VISIT_RECORDED, true);
                log.debug("Visit recorded for session: {}", session.getId());
            } catch (Exception e) {
                log.error("Error recording visit: ", e);
            }
        }
        
        return true;
    }
}
