package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import org.springframework.stereotype.Service;

/**
 * @author wAnG
 * @Date 2023-06-14  00:21
 */
@Service
public class Img2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Img2ImgCmd> {
    @Override
    public Task convertTask(Img2ImgCmd img2ImgCmd) {
        return null;
    }
}