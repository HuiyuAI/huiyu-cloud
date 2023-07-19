package com.huiyu.service.core.sd.submit;

import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmd2DtoConverter;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.utils.IdUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wAnG
 * @Date 2023-06-14  00:32
 */
@Service
public class Txt2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Txt2ImgCmd> {

    @Override
    public Pair<Task, Dto> convertTask(Txt2ImgCmd txt2ImgCmd) {
        Task task = new Task();
        Txt2ImgDto txt2ImgDto = SDCmd2DtoConverter.convert(txt2ImgCmd);

        LocalDateTime now = LocalDateTime.now();
        task.setId(IdUtils.nextSnowflakeId());
        task.setUserId(txt2ImgCmd.getUserId());
        task.setType(TaskTypeEnum.TXT2IMG);
        task.setStatus(TaskStatusEnum.UN_EXECUTED);
        task.setRetryCount(0);
        task.setPoint(txt2ImgCmd.getPoint());
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setIsDelete(0);
        task.setNum(txt2ImgCmd.getCount());
        return Pair.of(task, txt2ImgDto);
    }

}
