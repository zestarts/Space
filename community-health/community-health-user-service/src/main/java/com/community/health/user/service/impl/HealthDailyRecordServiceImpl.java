package com.community.health.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.health.common.entity.HealthDailyRecord;
import com.community.health.user.mapper.HealthDailyRecordMapper;
import com.community.health.user.service.HealthDailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthDailyRecordServiceImpl implements HealthDailyRecordService {

    private final HealthDailyRecordMapper mapper;

    @Override
    @Transactional
    public HealthDailyRecord saveOrUpdate(Long userId, HealthDailyRecord record) {
        record.setUserId(userId);
        LocalDate recordDate = record.getRecordDate();
        if (recordDate == null) recordDate = LocalDate.now();
        record.setRecordDate(recordDate);

        HealthDailyRecord existing = getByDate(userId, recordDate);
        if (existing != null) {
            record.setId(existing.getId());
            record.setVersion(existing.getVersion());
            mapper.updateById(record);
            return mapper.selectById(existing.getId());
        } else {
            mapper.insert(record);
            return record;
        }
    }

    @Override
    public HealthDailyRecord getByDate(Long userId, LocalDate date) {
        return mapper.selectOne(new LambdaQueryWrapper<HealthDailyRecord>()
                .eq(HealthDailyRecord::getUserId, userId)
                .eq(HealthDailyRecord::getRecordDate, date));
    }

    @Override
    public List<HealthDailyRecord> getRecent(Long userId, int days) {
        return mapper.selectList(new LambdaQueryWrapper<HealthDailyRecord>()
                .eq(HealthDailyRecord::getUserId, userId)
                .ge(HealthDailyRecord::getRecordDate, LocalDate.now().minusDays(days))
                .orderByDesc(HealthDailyRecord::getRecordDate));
    }
}