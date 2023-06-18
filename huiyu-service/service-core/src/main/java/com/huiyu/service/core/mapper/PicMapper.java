package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.Pic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PicMapper {
    List<Pic> getByUserId(@Param("userId") Long userId);

    Long getParentPicIdById(Long id);

    int insert(Pic pic);

    int updateByUuid(Pic pic);

    int deleteByUuid(String uuid);
}
