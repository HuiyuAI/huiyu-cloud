package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.model.dto.PicSharePageDto;
import com.huiyu.service.core.model.vo.PicSharePageVo;
import com.huiyu.service.core.model.vo.PicShareVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 图片分享表(PicShare)Mapper接口
 *
 * @author Naccl
 * @date 2023-08-19
 */
@Mapper
public interface PicShareMapper extends BaseMapper<PicShare> {

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param dto            查询条件
     * @param picShareStatus 图片投稿状态
     * @return 分页结果
     */
    IPage<PicSharePageVo> queryPage(@Param("page") IPage<PicShare> page, @Param("dto") PicSharePageDto dto, @Param("picShareStatus") PicShareStatusEnum picShareStatus);

    /**
     * 根据图片uuid查询图片分享详情
     *
     * @param uuid           图片uuid
     * @param picShareStatus 图片投稿状态
     * @return 图片分享详情
     */
    PicShareVo getByUuid(@Param("uuid") String uuid, @Param("picShareStatus") PicShareStatusEnum picShareStatus);
}
