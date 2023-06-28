package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.PicExt;
import com.huiyu.service.core.mapper.PicExtMapper;
import com.huiyu.service.core.service.PicExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PicExtServiceImpl implements PicExtService {

    @Resource
    private PicExtMapper picExtMapper;

    @Override
    public PicExt getByPicId(Long picId) {
        return null;
    }

    @Override
    public boolean insert(PicExt picExt) {
        return picExtMapper.insert(picExt) > 0;
    }

    @Override
    public boolean update(PicExt picExt) {
        return picExtMapper.update(picExt) > 0;
    }
}
