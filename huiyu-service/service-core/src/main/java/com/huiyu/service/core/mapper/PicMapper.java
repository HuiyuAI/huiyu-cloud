package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.Pic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PicMapper {
    List<Pic> getByUserId(@Param("userId") String userId);

    int update(Pic pic);
}
