package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.constant.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdConverter;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.utils.IdUtils;
import com.huiyu.service.core.utils.NewPair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wAnG
 * @Date 2023-06-14  00:32
 */
@Service
public class Txt2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Txt2ImgCmd> {
    @Override
    public NewPair<Task, Dto> convertTask(Txt2ImgCmd txt2ImgCmd) {
        Task task = new Task();
        Txt2ImgDto txt2ImgDto = SDCmdConverter.convert(txt2ImgCmd);

        LocalDateTime now = LocalDateTime.now();
        task.setId(IdUtils.nextSnowflakeId());
        task.setUserId(txt2ImgCmd.getUserId());
        task.setType(TaskTypeEnum.TXT2IMG);
        task.setStatus(TaskStatusEnum.IN_QUEUE);
        task.setExecSource("local");
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setIsDelete(0);
        task.setNum(txt2ImgCmd.getCount());
        return new NewPair<>(task, txt2ImgDto);
    }
}
