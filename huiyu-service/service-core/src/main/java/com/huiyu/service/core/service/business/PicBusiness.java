package com.huiyu.service.core.service.business;

import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;

public interface PicBusiness {

    boolean share(Pic pic, StateEnum state);
}
