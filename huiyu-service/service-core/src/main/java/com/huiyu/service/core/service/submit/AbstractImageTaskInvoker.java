package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.sd.dto.ImgDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wAnG
 * @Date 2023-06-13  00:07
 */
@Service
public abstract class AbstractImageTaskInvoker<T extends ImgDTO> {

    @Resource
    private ImageTaskService imageTaskService;

    public void invokerGenerate(Task task){
        insertTask();

        T t = buildRequest(task);

        invokerHttp();

        generateEnd();

        findTask();
    }

    private void generateEnd() {
        // todo 结束以后将任务置为完成
    }

    private void insertTask() {
        // todo 在这里插入执行记录
    }

    private void findTask() {
        // todo 寻找新的任务放入线程池

        imageTaskService.trySplitTask(new Task());

    }

    private void invokerHttp() {
        String url = getUrl();
    }

    private String getUrl() {
        // todo 多数据源操作后续会放在threadLocal里面
        return StringUtils.EMPTY;
    }

    public abstract T buildRequest(Task task);

    public abstract boolean isSupper(Task task);


}
