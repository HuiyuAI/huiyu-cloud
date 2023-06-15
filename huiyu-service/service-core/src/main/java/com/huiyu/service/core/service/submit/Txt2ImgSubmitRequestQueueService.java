package com.huiyu.service.core.service.submit;

import cn.hutool.json.JSONUtil;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import org.springframework.stereotype.Service;

/**
 * @author wAnG
 * @Date 2023-06-14  00:32
 */
@Service
public class Txt2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Txt2ImgCmd> {
    @Override
    public Task convertTask(Txt2ImgCmd txt2ImgCmd) {
        return Task.builder()
                .body(JSONUtil.toJsonStr(txt2ImgCmd.toDto()))
                .count(txt2ImgCmd.getCount())
                .status(TaskStatusEnum.IN_QUEUE)
                .build();
    }
}
