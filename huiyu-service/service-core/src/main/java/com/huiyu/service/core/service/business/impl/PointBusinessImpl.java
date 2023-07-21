package com.huiyu.service.core.service.business.impl;

import cn.hutool.core.util.IdUtil;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.PointRecordService;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.PointBusiness;
import com.huiyu.service.core.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class PointBusinessImpl implements PointBusiness {

    @Resource
    private UserService userService;

    @Resource
    private PointRecordService pointRecordService;


    /**
     * 更新积分
     *
     * @param userId    更新的用户id
     * @param point     需要操作的积分数值
     * @param source    操作来源
     * @param operation 积分增减
     * @param task      任务
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePoint(Long userId, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, Task task) {
        if (point == null || point <= 0) {
            throw new IllegalArgumentException("积分数值不合法");
        }

        if (operation == PointOperationTypeEnum.REDUCE) {
            point = -point;
        }

        // 修改用户积分
        boolean isUpdatePointOK = userService.updatePointByUserId(userId, point);
        if (!isUpdatePointOK) {
            log.error("更新用户积分失败，userId：{}，point：{}, source：{}, operation：{}", userId, point, source, operation);
            throw new RuntimeException("异常错误");
        }

        // 关联请求uuid
        String requestUuid = task == null ? "" : task.getRequestUuid();

        // 记录积分表
        LocalDateTime now = LocalDateTime.now();
        PointRecord pointRecord = PointRecord.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(userId)
                .requestUuid(requestUuid)
                .num(Math.abs(point))
                .operationType(operation)
                .operationSource(source)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean isInsertPointRecordOK = pointRecordService.insertRecord(pointRecord);
        if (!isInsertPointRecordOK) {
            log.error("记录积分流水表失败，userId：{}，point：{}, source：{}, operation：{}", userId, point, source, operation);
            throw new RuntimeException("异常错误");
        }

        return true;
    }
}
