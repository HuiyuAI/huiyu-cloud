package com.huiyu.service.core.sd.generate;

import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.entity.IntegralRecord;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.service.IntegralRecordService;
import com.huiyu.service.core.service.auth.UserService;
import com.huiyu.service.core.service.submit.AbstractSubmitRequestQueueService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wAnG
 * @Date 2023-06-12  00:59
 */
public abstract class AbstractImageGenerate<T extends Cmd> implements ImageGenerateService<T> {

    @Resource
    private List<AbstractSubmitRequestQueueService<T>> submitRequestQueueServiceList;

    @Resource
    private UserService userService;

    @Resource
    private IntegralRecordService integralRecordService;

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
        // 扣除用户积分
        Integer integral = t.getIntegral();
        User user = User.builder()
                .id(1L)
                .integral(integral)
                .build();
        userService.update(user);

        // 记录积分表
        IntegralRecord integralRecord = IntegralRecord.builder()
                .userId("1")
                .recordNo("")
                .fraction(integral)
                .operationType(IntegralOperationRecordEnum.REDUCE)
                .operationSource(IntegralSourceRecordEnum.GENERATE_PIC)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDelete(0)
                .build();
        integralRecordService.insertRecord(integralRecord);
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
}
