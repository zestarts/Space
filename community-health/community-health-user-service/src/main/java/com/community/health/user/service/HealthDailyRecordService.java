package com.community.health.user.service;

import com.community.health.common.entity.HealthDailyRecord;
import java.time.LocalDate;
import java.util.List;

public interface HealthDailyRecordService {
    HealthDailyRecord saveOrUpdate(Long userId, HealthDailyRecord record);
    HealthDailyRecord getByDate(Long userId, LocalDate date);
    List<HealthDailyRecord> getRecent(Long userId, int days);
}