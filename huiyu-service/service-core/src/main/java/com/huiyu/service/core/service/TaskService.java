package com.huiyu.service.core.service;

import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;

import java.util.List;


public interface TaskService {
    List<Task> getByStatus(TaskStatusEnum status, int limit, String source);

    boolean insertTask(Task task);

    boolean updateById(Task task);

    Task getById(Long taskId);
}
