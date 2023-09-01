package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.UserPicCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PicMapper extends BaseMapper<Pic> {
    Long getParentPicIdById(Long id);

    Pic getByTaskId(Long taskId);

    /**
     * 根据用户idList查询图片数量
     *
     * @param userIdList 用户idList
     * @return 图片数量
     */
    List<UserPicCountDto> countByUserIdList(@Param("userIdList") List<Long> userIdList);
}
