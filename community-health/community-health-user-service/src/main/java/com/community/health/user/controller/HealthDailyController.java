package com.community.health.user.controller;

import com.community.health.common.dto.Result;
import com.community.health.common.entity.HealthDailyRecord;
import com.community.health.user.service.HealthDailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/health/daily")
@RequiredArgsConstructor
public class HealthDailyController {

    private final HealthDailyRecordService service;

    @PostMapping
    public Result<HealthDailyRecord> saveOrUpdate(Authentication auth, @RequestBody HealthDailyRecord record) {
        Long userId = Long.parseLong(auth.getName());
        return Result.ok(service.saveOrUpdate(userId, record));
    }

    @GetMapping("/today")
    public Result<HealthDailyRecord> today(Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return Result.ok(service.getByDate(userId, LocalDate.now()));
    }

    @GetMapping("/recent")
    public Result<List<HealthDailyRecord>> recent(Authentication auth, @RequestParam(defaultValue = "7") int days) {
        Long userId = Long.parseLong(auth.getName());
        return Result.ok(service.getRecent(userId, days));
    }

    @GetMapping("/history")
    public Result<Map<String, Object>> history(Authentication auth,
                                                @RequestParam(defaultValue = "2024-01-01") String start,
                                                @RequestParam(defaultValue = "2099-12-31") String end) {
        Long userId = Long.parseLong(auth.getName());
        List<HealthDailyRecord> records = service.getRecent(userId, 365);
        return Result.ok(Map.of("records", records));
    }
}