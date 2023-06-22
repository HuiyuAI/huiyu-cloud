package com.huiyu.service.core.sd.generate;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.service.bussiness.IntegralRecordBussiness;
import com.huiyu.service.core.service.submit.AbstractSubmitRequestQueueService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wAnG
 * @Date 2023-06-12  00:59
 */
public abstract class AbstractImageGenerate<T extends Cmd> implements ImageGenerateService<T> {

    @Resource
    private List<AbstractSubmitRequestQueueService<T>> submitRequestQueueServiceList;

    @Resource
    private IntegralRecordBussiness integralRecordBussiness;

    @Override
    public void generate(T t) {
        preExec(t);

        submitTask(t);

        afterExec();
    }

    private void submitTask(T t) {
        submitRequestQueueServiceList.stream()
                .filter(service -> service.isSupport(t))
                .forEach(service -> service.submitToSplit(t));
    }

    public void preExec(T t) {
        boolean insertResult = changeUserIntegral(t);
    }

    public void afterExec() {
    }

    public boolean isSupport(T t) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        String name = t.getClass().getName();
        return StringUtils.equals(typeName, name);
    }

    private boolean changeUserIntegral(T t) {
        return integralRecordBussiness.updateIntegral(1L, t.getIntegral(),
                IntegralSourceRecordEnum.GENERATE_PIC, IntegralOperationRecordEnum.REDUCE);
    }
}
