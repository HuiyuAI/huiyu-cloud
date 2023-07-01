package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.Pic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PicMapper extends BaseMapper<Pic> {
    List<Pic> getByUserId(@Param("userId") Long userId);

    Long getParentPicIdById(Long id);

    int updateByUuid(Pic pic);

    int deleteByUuid(String uuid);

    Pic getByUuid(String uuid);

    Pic getByUuidOnly(String uuid);

    Pic getByTaskId(Long taskId);
}
