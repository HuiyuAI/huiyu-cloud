package com.huiyu.service.core.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.model.vo.UserAdminVo;
import com.huiyu.service.core.model.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户表(User)类型转换器
 *
 * @author Naccl
 * @date 2023-07-19
 */
@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * entity转vo
     *
     * @param source entity
     * @return vo
     */
    UserVo toVo(User source);

    /**
     * entity转vo
     *
     * @param source entity
     * @return vo
     */
    UserAdminVo toAdminVo(User source);

    /**
     * entity集合转vo集合
     *
     * @param sourceList entity集合
     * @return vo集合
     */
    List<UserAdminVo> toAdminVOList(List<User> sourceList);

    /**
     * entity分页转vo分页
     *
     * @param sourcePage entity分页
     * @return vo分页
     */
    Page<UserAdminVo> toAdminVOPage(IPage<User> sourcePage);

}
