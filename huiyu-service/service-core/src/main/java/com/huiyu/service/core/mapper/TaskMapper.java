package com.huiyu.service.core.mapper;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskMapper {

    List<Task> getByStatus(@Param("status") TaskStatusEnum status, @Param("limit") int limit, @Param("source") String source);

    int insertTask(Task task);

    int updateById(Task task);

    int batchUpdateBySource(@Param("replaceExecSource") String replaceExecSource
            , @Param("targetExecSource") String targetExecSource, @Param("limit") long limit);

    Task getById(Long taskId);
}
