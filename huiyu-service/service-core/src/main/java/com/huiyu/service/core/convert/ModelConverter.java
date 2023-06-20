package com.huiyu.service.core.convert;

import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.model.vo.ModelVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 模型表(Model)类型转换器
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Mapper
public interface ModelConverter {
    ModelConverter INSTANCE = Mappers.getMapper(ModelConverter.class);

    /**
     * entity转vo
     *
     * @param source entity
     * @return vo
     */
    ModelVo toVO(Model source);

    /**
     * entity集合转vo集合
     *
     * @param sourceList entity集合
     * @return vo集合
     */
    List<ModelVo> toVOList(List<Model> sourceList);

    /**
     * dto转entity
     *
     * @param source dto
     * @return entity
     */
    Model toEntity(ModelDto source);

    /**
     * dto集合转entity集合
     *
     * @param sourceList dto集合
     * @return entity集合
     */
    List<Model> toEntityList(List<ModelDto> sourceList);

}
