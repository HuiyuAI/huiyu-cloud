package com.huiyu.service.core.sd.generate;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.validator.AbstractCmdValidator;
import com.huiyu.service.core.service.submit.AbstractSubmitRequestQueueService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wAnG
 * @Date 2023-06-12  00:59
 */
public abstract class AbstractImageGenerate<T extends Cmd> extends AbstractCmdValidator<T> implements ImageGenerateService<T> {

    @Resource
    private List<AbstractSubmitRequestQueueService<T>> submitRequestQueueServiceList;

    @Override
    public void generate(T t) {
        preExec();

        boolean integralResult = validatorIntegral();
        if (!integralResult) {
            return;
        }

        boolean validateResult = validatorProperty(t);

        if (!validateResult) {
            return;
        }

        submitTask(t);

        afterExec();

    }

    private void submitTask(T t) {
        submitRequestQueueServiceList.stream()
                .filter(service -> service.isSupport(t))
                .forEach(service -> service.submitToSplit(t));
    }

    private boolean validatorIntegral() {
        return false;
    }


    public void preExec() {
    }

    public void afterExec(){
    };


}
