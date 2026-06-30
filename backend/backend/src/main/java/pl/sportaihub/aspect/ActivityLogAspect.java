package pl.sportaihub.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import pl.sportaihub.service.ActivityService;
/**
 * Aspekt rejestrujący zdarzenia biznesowe wykonywane w aplikacji.
 *
 * <p>Przechwytuje wykonanie metod oznaczonych adnotacją
 * {@link LogActivity}, mierzy czas ich wykonania oraz zapisuje
 * informację w historii aktywności. Zdarzenia są następnie
 * przekazywane do klientów przez WebSocket.</p>
 */
@Aspect
@Component
public class ActivityLogAspect {

    private final ActivityService activityService;

    public ActivityLogAspect(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Around("@annotation(logActivity)")
    public Object logActivity(
            ProceedingJoinPoint joinPoint,
            LogActivity logActivity
    ) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;

        String fullMessage =
                logActivity.message()
                        + " | metoda: "
                        + joinPoint.getSignature().getName()
                        + " | czas: "
                        + duration
                        + " ms";

        activityService.create(logActivity.type(), fullMessage);

        return result;
    }
}