package com.huiyu.service.core.service.submit;

import cn.hutool.json.JSONUtil;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.constant.ImageSizeEnum;
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
        Txt2ImgDto txt2ImgDto = new Txt2ImgDto();

        txt2ImgDto.setSdModelCheckpoint(txt2ImgCmd.getModelId());
        txt2ImgDto.setSdVae("");
        txt2ImgDto.setPrompt(txt2ImgCmd.getPrompt());
        txt2ImgDto.setNegativePrompt(txt2ImgCmd.getNegativePrompt());
        txt2ImgDto.setSamplerName("");
        txt2ImgDto.setSteps(txt2ImgCmd.getSteps());

        txt2ImgDto.setHrScale(null);
        txt2ImgDto.setDenoisingStrength(null);
        txt2ImgDto.setHrScale(null);

        // 图片尺寸
        Integer size = txt2ImgCmd.getSize();
        ImageSizeEnum imageSizeEnum = ImageSizeEnum.getEnumByCode(size);
        txt2ImgDto.setHeight(imageSizeEnum.getHigh());
        txt2ImgDto.setWidth(imageSizeEnum.getWight());

        boolean enableHr = false;

        switch (txt2ImgCmd.getQuality()) {
            case 1:
                // 高清
                enableHr = false;
                break;
            case 2:
                // 超清
                enableHr = true;
                break;
            case 3:
                // 超清修复 第一道工序+第二道工序
                enableHr = true;
                break;
            case 4:
                // 超清精绘 三道工序
                enableHr = true;
                break;
            default:
                break;
        }

        txt2ImgDto.setEnableHr(enableHr);
        txt2ImgDto.setCfgScale(txt2ImgCmd.getCfg());
        txt2ImgDto.setSeed(txt2ImgCmd.getSeed());

        task.setExecSource("local");
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setIsDelete(0);
        task.setUserId(txt2ImgCmd.getUserId());
        task.setCount(txt2ImgCmd.getCount());
        task.setTaskId(IdUtils.getUuId());
        task.setBody(JSONUtil.toJsonStr(txt2ImgDto));
        task.setStatus(TaskStatusEnum.UN_EXECUTED);
        return task;
    }
}
