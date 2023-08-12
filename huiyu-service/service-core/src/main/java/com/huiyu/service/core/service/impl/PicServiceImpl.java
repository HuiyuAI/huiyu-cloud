package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.mapper.PicMapper;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.dto.UserPicCountDto;
import com.huiyu.service.core.model.query.PicQuery;
import com.huiyu.service.core.service.PicExtService;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.utils.mirai.MiraiUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PicServiceImpl extends ServiceImpl<PicMapper, Pic> implements PicService {

    @Resource
    private PicMapper picMapper;

    @Resource
    private PicExtService picExtService;

    @Override
    public IPage<Pic> adminPageQuery(IPage<Pic> page, PicQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, Pic::getId, query.getId())
                .eq(StringUtils.isNotEmpty(query.getUuid()), Pic::getUuid, query.getUuid())
                .eq(StringUtils.isNotEmpty(query.getRequestUuid()), Pic::getRequestUuid, query.getRequestUuid())
                .eq(query.getUserId() != null, Pic::getUserId, query.getUserId())
                .eq(query.getTaskId() != null, Pic::getTaskId, query.getTaskId())
                .eq(query.getModelId() != null, Pic::getModelId, query.getModelId())
                .eq(query.getStatus() != null, Pic::getStatus, query.getStatus())
                .eq(query.getType() != null, Pic::getType, query.getType())
                .eq(query.getQuality() != null, Pic::getQuality, query.getQuality())
                .eq(query.getRatio() != null, Pic::getRatio, query.getRatio())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, Pic::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, Pic::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(Pic::getId)
                .page(page);
    }

    @Override
    public IPage<Pic> queryPage(IPage<Pic> page, PicPageDto dto) {
        return super.lambdaQuery()
                .select(Pic::getUuid, Pic::getPath, Pic::getStatus, Pic::getWidth, Pic::getHeight)
                .eq(Pic::getUserId, dto.getUserId())
                .le(Pic::getCreateTime, dto.getQueryDeadline())
                .orderByDesc(Pic::getCreateTime)
                .page(page);
    }

    @Override
    public Pic getByUuidOnly(String uuid) {
        return super.lambdaQuery()
                .eq(Pic::getUuid, uuid)
                .one();
    }

    @Override
    public Pic getByUuidAndUserId(String uuid, Long userId) {
        return super.lambdaQuery()
                .eq(Pic::getUuid, uuid)
                .eq(Pic::getUserId, userId)
                .one();
    }

    @Override
    public Pic getByUuidAndUserIdAndStatus(String uuid, Long userId, PicStatusEnum status) {
        return super.lambdaQuery()
                .eq(Pic::getUuid, uuid)
                .eq(Pic::getUserId, userId)
                .eq(Pic::getStatus, status)
                .one();
    }

    @Override
    public Pic getByTaskId(Long taskId) {
        return picMapper.getByTaskId(taskId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id) {
        int delete = picMapper.delete(id);
        if (delete <= 0) {
            return false;
        }
        return picExtService.deleteByPicId(id);
    }

    @Override
    public List<Pic> getPicsByUserId(Long userId) {
        return picMapper.getByUserId(userId);
    }

    @Override
    public Long getParentPicIdById(Long id) {
        Long parentPicId = picMapper.getParentPicIdById(id);
        if (parentPicId == null || parentPicId == 0 || parentPicId.equals(id)) {
            return id;
        }
        return getParentPicIdById(parentPicId);
    }

    @Override
    public List<UserPicCountDto> countByUserIdList(List<Long> userIdList) {
        return super.baseMapper.countByUserIdList(userIdList);
    }

    @Override
    public boolean insert(Pic pic) {
        return super.save(pic);
    }

    @Override
    public boolean updateByUuid(Pic pic) {
        return super.lambdaUpdate()
                .eq(Pic::getUuid, pic.getUuid())
                .update(pic);
    }

    @Override
    public void sendMsgByPicGenerated(String uuid) {
        Pic pic = getByUuidOnly(uuid);
        if (pic == null) {
            return;
        }
        MiraiUtils.sendMsgByPicGenerated(pic);
    }

    @Override
    public boolean deleteByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return false;
        }
        return picMapper.deleteByUuid(uuid) > 0;
    }

}
