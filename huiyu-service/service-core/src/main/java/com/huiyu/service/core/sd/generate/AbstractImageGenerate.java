package com.huiyu.service.core.sd.generate;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.validator.AbstractCmdValidator;

/**
 * @author wAnG
 * @Date 2023-06-12  00:59
 */
public abstract class AbstractImageGenerate<T extends Cmd> extends AbstractCmdValidator<T> implements ImageGenerateService<T>{

    @Override
    public void generate(T t){
        preExec();

        boolean integralResult = validatorIntegral();
        if(!integralResult){
            return;
        }

        boolean validateResult = validatorProperty(t);

        if (!validateResult){
            return;
        }

        submitTask();

        afterExec();

    }
    private void submitTask() {

    }

    private boolean validatorIntegral() {
        return false;
    }


    public  void preExec(){
        return;
    }

    public void afterExec(){
        return;
    };


}
