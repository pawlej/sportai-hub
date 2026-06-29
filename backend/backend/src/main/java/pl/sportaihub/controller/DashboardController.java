package pl.sportaihub.controller;

import org.springframework.web.bind.annotation.*;
import pl.sportaihub.dto.DashboardResponse;
import pl.sportaihub.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }
}