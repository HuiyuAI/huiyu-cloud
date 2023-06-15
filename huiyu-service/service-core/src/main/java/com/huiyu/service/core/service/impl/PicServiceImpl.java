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
    public List<Pic> getPicsByUserId(String userId) {
        return picMapper.getByUserId(userId);
    }

    @Override
    public boolean delete(Pic pic) {
        if(pic.getId() == null){
            return false;
        }
        pic.setIsDelete(TRUE);
        pic.setUpdateTime(LocalDateTime.now());
        int result = picMapper.update(pic);
        return result > 0;
    }

    @Override
    public Long getAncestorById(Long id) {
        Long ancestorId = picMapper.getAncestorById(id);
        if (ancestorId == null || ancestorId == 0 || ancestorId.equals(id)) {
            return id;
        }
        return getAncestorById(ancestorId);
    }
}
