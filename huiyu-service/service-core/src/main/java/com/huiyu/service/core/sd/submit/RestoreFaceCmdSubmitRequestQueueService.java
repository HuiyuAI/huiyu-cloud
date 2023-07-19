package com.huiyu.service.core.sd.submit;

import cn.hutool.core.lang.Pair;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.sd.SDCmd2DtoConverter;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.RestoreFaceDto;
import com.huiyu.service.core.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 脸部修复
 *
 * @author Naccl
 * @date 2023-07-19
 */
@Service
public class RestoreFaceCmdSubmitRequestQueueService extends AbstractSubmitRequestQueueService<RestoreFaceCmd> {

    @Override
    public Pair<Task, Dto> convertTask(RestoreFaceCmd cmd) {
        Task task = new Task();
        RestoreFaceDto dto = SDCmd2DtoConverter.convert(cmd);

        LocalDateTime now = LocalDateTime.now();
        task.setId(IdUtils.nextSnowflakeId());
        task.setUserId(cmd.getUserId());
        task.setType(TaskTypeEnum.RESTORE_FACE);
        task.setStatus(TaskStatusEnum.UN_EXECUTED);
        task.setRetryCount(0);
        task.setPoint(cmd.getPoint());
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setIsDelete(0);
        task.setNum(1);
        return Pair.of(task, dto);
    }

}
