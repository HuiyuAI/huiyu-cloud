package com.huiyu.service.core.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
     * entity转pageVO分页
     * 将Pic中ImageQualityEnum为UHD4K的图片，在PicPageVo中的is4k字段标记为true
     *
     * @param source entity
     * @return pageVO分页
     */
    @Mapping(target = "is4k", expression = "java(source.getQuality().is4k())")
    PicPageVo toPageVO(Pic source);

    /**
     * entity分页转vo分页
     *
     * @param sourcePage entity分页
     * @return vo分页
     */
    Page<PicPageVo> toVOPage(IPage<Pic> sourcePage);

}
