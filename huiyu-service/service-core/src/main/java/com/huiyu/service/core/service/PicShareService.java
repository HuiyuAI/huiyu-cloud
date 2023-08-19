package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.PicShareStatusEnum;
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
     * 根据用户id和图片uuid查询图片分享详情
     *
     * @param userId   用户id
     * @param uuidList 图片uuidList
     * @return 详情
     */
    List<PicShare> getByUserIdAndUuidList(Long userId, List<String> uuidList);

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
}
