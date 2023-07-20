package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.mapper.TaskMapper;
import com.huiyu.service.core.model.query.TaskQuery;
import com.huiyu.service.core.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    @Override
    public IPage<Task> adminPageQuery(IPage<Task> page, TaskQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, Task::getId, query.getId())
                .eq(StringUtils.isNotEmpty(query.getRequestUuid()), Task::getRequestUuid, query.getRequestUuid())
                .eq(query.getUserId() != null, Task::getUserId, query.getUserId())
                .eq(query.getType() != null, Task::getType, query.getType())
                .eq(query.getStatus() != null, Task::getStatus, query.getStatus())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, Task::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, Task::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(Task::getId)
                .page(page);
    }

    @Override
    public List<Task> getByStatus(TaskStatusEnum status, int limit, String source) {
        return taskMapper.getByStatus(status, limit, source);
    }

    @Override
    public boolean insertTask(Task task) {
        return taskMapper.insertTask(task) > 0;
    }

    @Override
    public boolean updateById(Task task) {
        return taskMapper.updateById(task) > 0;
    }

    public boolean batchUpdateBySource(String replaceExecSource, String targetExecSource, long limit) {
        return taskMapper.batchUpdateBySource(replaceExecSource, targetExecSource, limit) > 0;
    }

    @Override
    public Task getById(Long taskId) {
        return taskMapper.getById(taskId);
    }
}
