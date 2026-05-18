package com.community.health.user.service;

import com.community.health.common.entity.HealthDailyRecord;
import java.time.LocalDate;
import java.util.List;

public interface HealthDailyRecordService {
    HealthDailyRecord saveOrUpdate(String username, HealthDailyRecord record);
    HealthDailyRecord getByDate(String username, LocalDate date);
    List<HealthDailyRecord> getRecent(String username, int days);
}