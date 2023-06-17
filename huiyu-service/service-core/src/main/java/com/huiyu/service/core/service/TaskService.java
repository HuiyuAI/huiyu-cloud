package com.huiyu.service.core.service;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;

import java.util.List;


public interface TaskService {
    List<Task> getByStatus(TaskStatusEnum status, int limit);

    boolean insertTask(Task task);

    boolean update(Task task);

    boolean updateByTaskId(Task task);
}
