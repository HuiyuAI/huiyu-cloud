package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.Task;

import java.util.List;


public interface TaskService {
    List<Task> getByStatus(int status, int n);

    boolean insertTask(Task task);

    boolean update(Task task);
}
