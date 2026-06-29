package pl.sportaihub.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import pl.sportaihub.service.ActivityService;

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