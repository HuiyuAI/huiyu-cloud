package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.mapper.PicMapper;
import com.huiyu.service.core.service.PicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PicServiceImpl implements PicService {

    private static final Integer TRUE = 1;

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
    public boolean delete(Pic pic) {
        if (pic.getId() == null) {
            return false;
        }
        pic.setIsDelete(TRUE);
        pic.setUpdateTime(LocalDateTime.now());
        int result = picMapper.update(pic);
        return result > 0;
    }
}
