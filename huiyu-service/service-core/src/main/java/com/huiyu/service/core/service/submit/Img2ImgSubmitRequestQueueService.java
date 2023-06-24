package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.Hconfig.AIExampleConfig;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.utils.NewPair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author wAnG
 * @Date 2023-06-14  00:21
 */
@Service
public class Img2ImgSubmitRequestQueueService extends AbstractSubmitRequestQueueService<Img2ImgCmd> {
    @Resource
    private AIExampleConfig aiExampleConfig;

    @Override
    public NewPair<Task, Dto> convertTask(Img2ImgCmd img2ImgCmd) {
        return null;
    }

    @Override
    public String chooseExecSource(Img2ImgCmd img2ImgCmd) {
        List<AIExampleConfig.ExampleItem> exampleItems = aiExampleConfig.getExampleItems();
        Random random = new Random();
        int i = random.nextInt(exampleItems.size());
        return exampleItems.get(i).getSource();
    }
}
