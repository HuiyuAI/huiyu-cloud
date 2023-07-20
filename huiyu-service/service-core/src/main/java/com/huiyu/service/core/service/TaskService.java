package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.query.TaskQuery;

import java.util.List;

public interface TaskService extends IService<Task> {
    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<Task> adminPageQuery(IPage<Task> page, TaskQuery query);

    List<Task> getByStatus(TaskStatusEnum status, int limit, String source);

    boolean insertTask(Task task);

    boolean updateById(Task task);

    Task getById(Long taskId);
}
