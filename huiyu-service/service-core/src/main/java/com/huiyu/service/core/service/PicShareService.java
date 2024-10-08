package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.model.dto.PicSharePageDto;
import com.huiyu.service.core.model.dto.UserPicShareCountDto;
import com.huiyu.service.core.model.query.PicShareQuery;
import com.huiyu.service.core.model.vo.PicShareAdminVo;
import com.huiyu.service.core.model.vo.PicSharePageVo;
import com.huiyu.service.core.model.vo.PicShareVo;
import com.huiyu.service.core.model.vo.RedrawVo;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 图片分享表(PicShare)服务接口
 *
 * @author Naccl
 * @date 2023-08-19
 */
public interface PicShareService extends IService<PicShare> {
    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<PicShareAdminVo> adminPageQuery(IPage<PicShare> page, PicShareQuery query);

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto  查询条件
     * @return 分页结果
     */
    IPage<PicSharePageVo> queryPage(IPage<PicShare> page, PicSharePageDto dto);

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
     * @param uuid 图片uuid
     * @return 隐藏参数
     */
    RedrawVo redraw(String uuid);

    /**
     * 根据图片id查询图片分享详情
     *
     * @param picId 图片id
     * @return 图片分享详情
     */
    PicShare getByPicId(Long picId);

    /**
     * 根据用户id和图片uuid查询图片分享详情
     *
     * @param userId   用户id
     * @param uuidList 图片uuidList
     * @return 详情
     */
    List<PicShare> getByUserIdAndUuidList(Long userId, List<String> uuidList);

    /**
     * 根据用户idList查询投稿数量
     *
     * @param userIdList 用户idList
     * @return 投稿数量
     */
    List<UserPicShareCountDto> countByUserIdList(List<Long> userIdList);

    /**
     * 投稿分享图片
     *
     * @param picShare 实体对象
     * @return true/false
     */
    boolean save(PicShare picShare);

    /**
     * 投稿后通知审核群
     *
     * @param pic       图片
     * @param picShare  投稿详情
     * @param shareTime 投稿时间
     */
    @Async
    void sendMsgByPicShare(Pic pic, PicShare picShare, LocalDateTime shareTime);

    /**
     * 审核
     *
     * @param picIdList 图片主键idList
     * @param status    审核结果
     * @return true/false
     */
    boolean audit(List<Long> picIdList, PicShareStatusEnum status);

    /**
     * 重新审核
     *
     * @param picIdList 图片主键idList
     * @return true/false
     */
    boolean reAudit(List<Long> picIdList);

    /**
     * 根据图片uuid增加图片点击量
     *
     * @param uuid 图片uuid
     */
    @Async
    void addHitsByUuid(String uuid);

    /**
     * 根据图片uuid增加图片画同款次数
     *
     * @param uuid 图片uuid
     */
    @Async
    void addRedrawCountByUuid(String uuid);
}
