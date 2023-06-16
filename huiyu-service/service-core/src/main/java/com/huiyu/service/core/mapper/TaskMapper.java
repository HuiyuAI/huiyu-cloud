package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskMapper {

    List<Task> getByStatus(@Param("status") int status, @Param("limit") int limit);

    int insertTask(Task task);

    int update(Task task);

}
