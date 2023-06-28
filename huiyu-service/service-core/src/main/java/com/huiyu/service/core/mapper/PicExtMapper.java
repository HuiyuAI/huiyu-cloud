package com.huiyu.service.core.mapper;

import com.huiyu.service.core.entity.PicExt;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PicExtMapper {
    PicExt getByPicId(Long picId);

    int insert(PicExt picExt);

    int update(PicExt picExt);
}
