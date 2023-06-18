package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.mapper.PicMapper;
import com.huiyu.service.core.service.PicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PicServiceImpl implements PicService {

    @Resource
    private PicMapper picMapper;

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
    public boolean insert(Pic pic) {
        return picMapper.insert(pic) > 0;
    }

    @Override
    public boolean updateByUuid(Pic pic) {
        return picMapper.updateByUuid(pic) > 0;
    }

    @Override
    public boolean deleteByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return false;
        }
        return picMapper.deleteByUuid(uuid) > 0;
    }
}
