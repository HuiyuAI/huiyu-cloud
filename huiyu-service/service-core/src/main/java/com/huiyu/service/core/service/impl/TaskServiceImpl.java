package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.mapper.TaskMapper;
import com.huiyu.service.core.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    @Override
    public List<Task> getByStatus(TaskStatusEnum status, int limit) {
        return taskMapper.getByStatus(status, limit);
    }

    @Override
    public boolean insertTask(Task task) {
        return taskMapper.insertTask(task) > 0;
    }

    @Override
    public boolean updateById(Task task) {
        return taskMapper.updateById(task) > 0;
    }

    @Override
    public Task getById(Long taskId) {
        return taskMapper.getById(taskId);
    }
}
