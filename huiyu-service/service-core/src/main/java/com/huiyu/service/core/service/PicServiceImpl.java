package com.huiyu.service.core.service;

import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicExt;
import com.huiyu.service.core.mapper.PicExtMapper;
import com.huiyu.service.core.mapper.PicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PicServiceImpl implements PicService {

    private static final Integer TRUE = 1;

    @Resource
    private PicMapper picMapper;

    @Resource
    private PicExtMapper picExtMapper;

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

    /**
     * 分享投稿图片
     *
     * @param pic   图片信息
     * @param state 需要的状态
     * @return      是否操作成功
     */
    @Override
    public boolean share(Pic pic, StateEnum state) {
        if (pic.getUserId() == null || pic.getId() == null) {
            return false;
        }
        // 判断是否存在
        PicExt picExt = picExtMapper.getByPicId(pic.getId());
        if (picExt != null) {
            // 已分享
            picExt.setEnable(state);
            return picExtMapper.update(picExt) > 0;
        } else {
            // 未分享
            picExt = new PicExt();
            picExt.setPicId(pic.getId());
            picExt.setViews(0);
            picExt.setPath(pic.getPath());
            picExt.setEnable(state);
            return picExtMapper.insertPicExt(picExt) > 0;
        }
    }
}
