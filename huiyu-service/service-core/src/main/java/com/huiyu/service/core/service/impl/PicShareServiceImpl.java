package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.mapper.PicShareMapper;
import com.huiyu.service.core.model.dto.PicSharePageDto;
import com.huiyu.service.core.model.dto.UserPicShareCountDto;
import com.huiyu.service.core.model.query.PicShareQuery;
import com.huiyu.service.core.model.vo.PicShareAdminVo;
import com.huiyu.service.core.model.vo.PicSharePageVo;
import com.huiyu.service.core.model.vo.PicShareVo;
import com.huiyu.service.core.model.vo.RedrawVo;
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
    public IPage<PicShareAdminVo> adminPageQuery(IPage<PicShare> page, PicShareQuery query) {
        return super.baseMapper.adminPageQuery(page, query);
    }

    @Override
    public IPage<PicSharePageVo> queryPage(IPage<PicShare> page, PicSharePageDto dto) {
        return super.baseMapper.queryPage(page, dto, PicShareStatusEnum.PUBLIC);
    }

    @Override
    public PicShareVo getPicShareVoByUuid(String uuid) {
        return super.baseMapper.getByUuid(uuid, PicShareStatusEnum.PUBLIC);
    }

    @Override
    public RedrawVo redraw(String uuid) {
        return super.baseMapper.redraw(uuid, PicShareStatusEnum.PUBLIC);
    }

    @Override
    public PicShare getByPicId(Long picId) {
        return super.lambdaQuery()
                .eq(PicShare::getPicId, picId)
                .one();
    }

    @Override
    public List<PicShare> getByUserIdAndUuidList(Long userId, List<String> uuidList) {
        return super.lambdaQuery()
                .eq(PicShare::getUserId, userId)
                .in(PicShare::getUuid, uuidList)
                .list();
    }

    @Override
    public List<UserPicShareCountDto> countByUserIdList(List<Long> userIdList) {
        return super.baseMapper.countByUserIdList(userIdList);
    }

    @Override
    public boolean save(PicShare picShare) {
        picShare.setHits(0);
        picShare.setLikeCount(0);
        picShare.setDrawCount(0);
        picShare.setIsDelete(0);
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

    @Override
    public void addHitsByUuid(String uuid) {
        super.baseMapper.addHitsByUuid(uuid);
    }

    @Override
    public void addRedrawCountByUuid(String uuid) {
        super.baseMapper.addRedrawCountByUuid(uuid);
    }
}
