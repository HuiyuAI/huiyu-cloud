package com.huiyu.service.core.service.business;

import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;

public interface PicBusiness {

    /**
     * 图片分享
     *
     * @param pic   图片
     * @param state 分享状态
     * @return
     */
    boolean share(Pic pic, StateEnum state);
}
