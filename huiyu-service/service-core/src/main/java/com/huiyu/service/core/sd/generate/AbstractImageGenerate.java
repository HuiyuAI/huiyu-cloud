package com.huiyu.service.core.sd.generate;

import com.huiyu.service.core.entity.SdResponseContext;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.submit.AbstractSubmitRequestQueueService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wAnG
 * @Date 2023-06-12  00:59
 */
public abstract class AbstractImageGenerate<T extends Cmd> implements ImageGenerateService<T> {

    @Resource
    private List<AbstractSubmitRequestQueueService<T>> submitRequestQueueServiceList;

    @Override
    public void generate(T t, SdResponseContext responseContext) {
        preExec(t);

        List<CompletableFuture<Void>> futureList = submitTask(t, responseContext);

        afterExec(futureList, responseContext);
    }

    private List<CompletableFuture<Void>> submitTask(T t, SdResponseContext responseContext) {
        return submitRequestQueueServiceList.stream()
                .filter(service -> service.isSupport(t))
                .map(service -> service.submitToSplit(t, responseContext))
                .collect(Collectors.toList());
    }

    public void preExec(T t) {

    }

    public void afterExec(List<CompletableFuture<Void>> futureList, SdResponseContext responseContext) {
        unfinished(responseContext);

        futureList.forEach(future -> {
            try {
                // todo 粗超时时间，超时时间应该递减
                future.get(500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        futureCompleted(responseContext);
    }

    public void unfinished(SdResponseContext responseContext) {

    }

    public void futureCompleted(SdResponseContext responseContext) {

    }

    public boolean isSupport(T t) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        String name = t.getClass().getName();
        return StringUtils.equals(typeName, name);
    }
}
