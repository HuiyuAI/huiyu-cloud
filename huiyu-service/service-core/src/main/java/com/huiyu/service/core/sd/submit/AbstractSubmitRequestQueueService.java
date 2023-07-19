package com.huiyu.service.core.sd.submit;

import com.huiyu.service.core.hconfig.config.HotFileConfig;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.enums.PointOperationRecordEnum;
import com.huiyu.service.core.enums.PointSourceRecordEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.service.business.PointBusiness;
import com.huiyu.service.core.sd.submit.chooseStrategy.ExecChooseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.huiyu.service.core.config.executor.CompletableFutureExceptionHandle.ExceptionLogHandle;

/**
 * @author wAnG
 * @Date 2023-06-12  23:30
 */
@Slf4j
public abstract class AbstractSubmitRequestQueueService<T extends Cmd> {

    @Resource
    @Qualifier("splitTaskExecutor")
    private ThreadPoolExecutorDecorator splitTaskExecutor;

    @Resource
    private ImageTaskService imageTaskService;

    @Resource
    private List<ExecChooseStrategy<T>> execChooseStrategyList;

    @Resource
    private PointBusiness pointBusiness;

    @Resource
    private HotFileConfig hotFileConfig;

    public void submitToSplit(T t) {
        Pair<Task, Dto> taskDtoPair = convertTask(t);
        Integer execStrategy = hotFileConfig.getExecStrategy();
        String execSource = execChooseStrategyList.stream()
                .filter(strategy -> Objects.equals(strategy.getType().getCode(), execStrategy))
                .map(strategy -> strategy.chooseExecSource(t))
                .findFirst()
                .orElse(null);
        Task task = taskDtoPair.getFirst();
        task.setExecSource(execSource);
        String requestUuid = RequestContext.REQUEST_UUID_CONTEXT.get();
        task.setRequestUuid(requestUuid);
        if (!deductUserPoint(task)) {
            return;
        }
        Dto dto = taskDtoPair.getSecond();
        CompletableFuture.runAsync(() -> imageTaskService.trySplitTask(task, dto), splitTaskExecutor.getThreadPoolExecutor())
                .exceptionally(ExceptionLogHandle);
    }

    public boolean isSupport(T t) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        String name = t.getClass().getName();
        return StringUtils.equals(typeName, name);
    }

    public abstract Pair<Task, Dto> convertTask(T t);

    private boolean deductUserPoint(Task task) {
        return pointBusiness.updatePoint(task.getUserId(), task.getPoint(), PointSourceRecordEnum.GENERATE_PIC, PointOperationRecordEnum.REDUCE, task);
    }
}
