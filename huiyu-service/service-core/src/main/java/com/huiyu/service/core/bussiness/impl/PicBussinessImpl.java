package com.huiyu.service.core.bussiness.impl;

import com.huiyu.service.core.bussiness.PicBussiness;
import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicExt;
import com.huiyu.service.core.service.PicExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PicBussinessImpl implements PicBussiness {

    @Resource
    private PicExtService picExtService;

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
        PicExt picExt = picExtService.getByPicId(pic.getId());
        if (picExt != null) {
            // 已分享
            picExt.setEnable(state);
            return picExtService.update(picExt) > 0;
        } else {
            // 未分享
            picExt = new PicExt();
            picExt.setPicId(pic.getId());
            picExt.setViews(0);
            picExt.setPath(pic.getPath());
            picExt.setEnable(state);
            return picExtService.insertPicExt(picExt) > 0;
        }
    }
}
