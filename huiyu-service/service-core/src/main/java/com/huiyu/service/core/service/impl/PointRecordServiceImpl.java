package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.mapper.PointRecordMapper;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.query.PointRecordQuery;
import com.huiyu.service.core.service.PointRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PointRecordServiceImpl extends ServiceImpl<PointRecordMapper, PointRecord> implements PointRecordService {

    @Override
    public IPage<PointRecord> adminPageQuery(IPage<PointRecord> page, PointRecordQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, PointRecord::getId, query.getId())
                .eq(StringUtils.isNotEmpty(query.getRequestUuid()), PointRecord::getRequestUuid, query.getRequestUuid())
                .eq(query.getUserId() != null, PointRecord::getUserId, query.getUserId())
                .eq(query.getOperationType() != null, PointRecord::getOperationType, query.getOperationType())
                .eq(query.getOperationSource() != null, PointRecord::getOperationSource, query.getOperationSource())
                .eq(query.getPointType() != null, PointRecord::getPointType, query.getPointType())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, PointRecord::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, PointRecord::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(PointRecord::getId)
                .page(page);
    }

    @Override
    public IPage<PointRecord> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto) {
        return super.lambdaQuery()
                .select(PointRecord::getDailyPoint, PointRecord::getPoint, PointRecord::getOperationType, PointRecord::getOperationSource, PointRecord::getCreateTime)
                .eq(PointRecord::getUserId, dto.getUserId())
                .orderByDesc(PointRecord::getCreateTime)
                .page(page);
    }

    @Override
    public PointRecord getByRequestUuid(String requestUuid) {
        return super.lambdaQuery()
                .eq(PointRecord::getRequestUuid, requestUuid)
                .one();
    }

    @Override
    public boolean insertRecord(PointRecord pointRecord) {
        LocalDateTime now = LocalDateTime.now();
        pointRecord.setCreateTime(now);
        pointRecord.setUpdateTime(now);
        pointRecord.setIsDelete(0);
        return super.save(pointRecord);
    }

    @Override
    public boolean updatePointRecord(PointRecord pointRecord) {
        pointRecord.setUpdateTime(LocalDateTime.now());
        return super.updateById(pointRecord);
    }

}
