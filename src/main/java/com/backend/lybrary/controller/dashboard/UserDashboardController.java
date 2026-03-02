package com.backend.lybrary.controller.dashboard;

import com.backend.lybrary.dto.dashboard.UserDashboardResponse;
import com.backend.lybrary.service.dashboard.UserDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/dashboard")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<UserDashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}
