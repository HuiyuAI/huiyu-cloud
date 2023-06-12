package com.huiyu.service.core.business.impl;

import com.huiyu.service.core.business.TaskBusiness;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 14:37
 * @Description: 任务表业务逻辑实现类
 **/
@Service
public class TaskBussinessImpl implements TaskBusiness {

    @Resource
    private TaskService taskService;

    @Override
    public List<Task> getByStatus(int status, int n) {
        return taskService.getByStatus(status, n);
    }

    @Override
    public boolean insertTask(Task task) {
        return taskService.insertTask(task);
    }

    @Override
    public boolean update(Task task) {
        return taskService.update(task);
    }
}
