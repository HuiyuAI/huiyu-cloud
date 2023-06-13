package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import org.springframework.stereotype.Service;

/**
 * @author wAnG
 * @Date 2023-06-14  00:32
 */
@Service
public class Txt2SubmitRequestQueueService extends AbstractSubmitRequestQueueService<Txt2ImgCmd> {
    @Override
    public Task convertTask(Txt2ImgCmd txt2ImgCmd) {
        return null;
    }
}
