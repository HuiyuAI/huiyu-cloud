package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.constant.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.sd.SDCmd2DtoConverter;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.RestoreFaceDto;
import com.huiyu.service.core.utils.IdUtils;
import com.huiyu.service.core.utils.NewPair;
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
    public NewPair<Task, Dto> convertTask(RestoreFaceCmd cmd) {
        Task task = new Task();
        RestoreFaceDto dto = SDCmd2DtoConverter.convert(cmd);

        LocalDateTime now = LocalDateTime.now();
        task.setId(IdUtils.nextSnowflakeId());
        task.setUserId(cmd.getUserId());
        task.setType(TaskTypeEnum.RESTORE_FACE);
        task.setStatus(TaskStatusEnum.UN_EXECUTED);
        task.setRetryCount(0);
        task.setIntegral(cmd.getPoint());
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setIsDelete(0);
        task.setNum(1);
        return new NewPair<>(task, dto);
    }

}
