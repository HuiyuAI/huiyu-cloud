package com.huiyu.service.core.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.dto.PicShareDto;
import com.huiyu.service.core.model.dto.PicSharePageDto;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicSharePageVo;
import com.huiyu.service.core.model.vo.PicShareVo;
import com.huiyu.service.core.model.vo.PicVo;
import com.huiyu.service.core.model.vo.RedrawVo;

import java.util.List;

public interface PicBusiness {

    /**
     * 图片生成完毕回调
     *
     * @param resImageUuid    图片uuid
     * @param resImageUrlUuid 图片地址uuid
     */
    void picGeneratedCallback(String resImageUuid, String resImageUrlUuid);

    /**
     * 分页查询画夹
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
     * @param userId      用户id
     * @param picShareDto 投稿信息
     * @return
     */
    boolean share(Long userId, PicShareDto picShareDto);

    /**
     * 用户批量删除图片
     *
     * @param userId   用户id
     * @param uuidList 图片uuid列表
     * @return true/false
     */
    boolean userDeleteByUuidList(Long userId, List<String> uuidList);

    /**
     * 分页查询广场投稿
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PicSharePageVo> picSharePage(IPage<PicShare> page, PicSharePageDto dto);

    /**
     * 根据图片uuid查询图片分享详情
     *
     * @param uuid 图片uuid
     * @return 图片分享详情
     */
    PicShareVo getPicShareVoByUuid(String uuid);

    /**
     * 画同款-获取图片隐藏参数
     *
     * @param userId 操作用户id
     * @param uuid   图片uuid
     * @return 隐藏参数
     */
    RedrawVo redraw(Long userId, String uuid);
}
