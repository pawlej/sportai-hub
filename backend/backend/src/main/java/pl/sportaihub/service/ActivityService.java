package pl.sportaihub.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sportaihub.dto.ActivityResponse;
import pl.sportaihub.entity.ActivityLog;
import pl.sportaihub.enums.ActivityType;
import pl.sportaihub.repository.ActivityLogRepository;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ActivityService(
            ActivityLogRepository activityLogRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.activityLogRepository = activityLogRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public ActivityResponse create(ActivityType type, String message) {
        ActivityLog activityLog = new ActivityLog(type, message);
        ActivityLog saved = activityLogRepository.save(activityLog);

        ActivityResponse response = toResponse(saved);

        messagingTemplate.convertAndSend("/topic/activity", response);

        return response;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findLatest() {
        return activityLogRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ActivityResponse toResponse(ActivityLog activityLog) {
        return new ActivityResponse(
                activityLog.getId(),
                activityLog.getType(),
                activityLog.getMessage(),
                activityLog.getCreatedAt()
        );
    }
}