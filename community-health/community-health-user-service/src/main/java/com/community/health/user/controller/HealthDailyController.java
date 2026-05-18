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
        return Result.ok(service.saveOrUpdate(auth.getName(), record));
    }

    @GetMapping("/today")
    public Result<HealthDailyRecord> today(Authentication auth) {
        return Result.ok(service.getByDate(auth.getName(), LocalDate.now()));
    }

    @GetMapping("/recent")
    public Result<List<HealthDailyRecord>> recent(Authentication auth, @RequestParam(defaultValue = "7") int days) {
        return Result.ok(service.getRecent(auth.getName(), days));
    }
}