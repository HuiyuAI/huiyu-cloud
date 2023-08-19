package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.mapper.PicShareMapper;
import com.huiyu.service.core.service.PicShareService;
import com.huiyu.service.core.utils.mirai.MiraiUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 图片分享表(PicShare)服务实现类
 *
 * @author Naccl
 * @date 2023-08-19
 */
@Service
public class PicShareServiceImpl extends ServiceImpl<PicShareMapper, PicShare> implements PicShareService {

    @Override
    public List<PicShare> getByUserIdAndUuidList(Long userId, List<String> uuidList) {
        return super.lambdaQuery()
                .eq(PicShare::getUserId, userId)
                .in(PicShare::getUuid, uuidList)
                .list();
    }

    @Override
    public boolean save(PicShare picShare) {
        return super.save(picShare);
    }

    @Override
    public void sendMsgByPicShare(Pic pic, PicShare picShare, LocalDateTime shareTime) {
        MiraiUtils.sendMsgByPicShare(pic, picShare, shareTime);
    }

    @Override
    public boolean audit(List<Long> picIdList, PicShareStatusEnum status) {
        return super.lambdaUpdate()
                .in(PicShare::getPicId, picIdList)
                .eq(PicShare::getStatus, PicShareStatusEnum.AUDITING)
                .set(PicShare::getStatus, status)
                .set(PicShare::getAuditTime, LocalDateTime.now())
                .update();
    }

    @Override
    public boolean reAudit(List<Long> picIdList) {
        return super.lambdaUpdate()
                .in(PicShare::getPicId, picIdList)
                .set(PicShare::getStatus, PicShareStatusEnum.AUDITING)
                .update();
    }
}
