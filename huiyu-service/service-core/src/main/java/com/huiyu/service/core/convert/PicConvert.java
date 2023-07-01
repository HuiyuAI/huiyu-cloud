package com.huiyu.service.core.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 图片表(Pic)类型转换器
 *
 * @author Naccl
 * @date 2023-06-25
 */
@Mapper
public interface PicConvert {
    PicConvert INSTANCE = Mappers.getMapper(PicConvert.class);

    /**
     * entity转vo
     *
     * @param source entity
     * @return vo
     */
    PicVo toVO(Pic source);

    /**
     * entity集合转vo集合
     *
     * @param sourceList entity集合
     * @return vo集合
     */
    List<PicVo> toVOList(List<Pic> sourceList);

    /**
     * entity分页转vo分页
     *
     * @param sourcePage entity分页
     * @return vo分页
     */
    Page<PicPageVo> toVOPage(IPage<Pic> sourcePage);

}
