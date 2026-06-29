package pl.sportaihub.controller;

import org.springframework.web.bind.annotation.*;
import pl.sportaihub.dto.ActivityResponse;
import pl.sportaihub.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<ActivityResponse> findLatest() {
        return activityService.findLatest();
    }
}