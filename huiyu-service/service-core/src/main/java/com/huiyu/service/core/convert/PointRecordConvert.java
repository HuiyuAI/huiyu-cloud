package com.huiyu.service.core.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 积分表(PointRecord)类型转换器
 *
 * @author Naccl
 * @date 2023-08-11
 */
@Mapper
public interface PointRecordConvert {
    PointRecordConvert INSTANCE = Mappers.getMapper(PointRecordConvert.class);

    /**
     * entity分页转vo分页
     *
     * @param sourcePage entity分页
     * @return vo分页
     */
    Page<PointRecordPageVo> toVOPage(IPage<PointRecord> sourcePage);

}
