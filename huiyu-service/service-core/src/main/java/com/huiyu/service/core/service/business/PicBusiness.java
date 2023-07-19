package com.huiyu.service.core.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.core.enums.StateEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicVo;

public interface PicBusiness {
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PicPageVo> queryVoPage(IPage<Pic> page, PicPageDto dto);

    /**
     * 查询图片详情
     *
     * @param uuid   图片uuid
     * @param userId 用户id
     * @return 图片详情
     */
    PicVo getPicVo(String uuid, Long userId);

    /**
     * 图片分享
     *
     * @param pic   图片
     * @param state 分享状态
     * @return
     */
    boolean share(Pic pic, StateEnum state);
}
