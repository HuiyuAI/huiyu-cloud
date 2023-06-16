package com.huiyu.service.core.service.submit;

import cn.hutool.json.JSONUtil;
import com.huiyu.common.web.util.RequestUtils;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.constant.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdConverter;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wAnG
 * @Date 2023-06-14  00:32
 */
@Service
public class Txt2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Txt2ImgCmd> {
    @Override
    public Task convertTask(Txt2ImgCmd txt2ImgCmd) {
        Task task = new Task();
        Txt2ImgDto txt2ImgDto = SDCmdConverter.convert(txt2ImgCmd);

        LocalDateTime now = LocalDateTime.now();
        task.setId(IdUtils.nextSnowflakeId());
        task.setUserId(txt2ImgCmd.getUserId());
        task.setType(TaskTypeEnum.TXT2IMG);
        task.setUrl(RequestUtils.getRequestURI());
        task.setBody(JSONUtil.toJsonStr(txt2ImgDto));
        task.setStatus(TaskStatusEnum.UN_EXECUTED);
        task.setExecSource("local");
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setIsDelete(0);
        task.setCount(txt2ImgCmd.getCount());
        return task;
    }
}
