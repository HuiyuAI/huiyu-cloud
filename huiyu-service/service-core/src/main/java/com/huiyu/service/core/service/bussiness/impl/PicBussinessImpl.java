package com.huiyu.service.core.service.bussiness.impl;

import com.huiyu.service.core.service.bussiness.PicBussiness;
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
            picExt = PicExt.builder()
                    .picId(pic.getId())
                    .views(0)
                    .path(pic.getPath())
                    .enable(state)
                    .build();
            return picExtService.insertPicExt(picExt) > 0;
        }
    }
}
