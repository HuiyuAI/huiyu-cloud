package com.huiyu.service.core.service.bussiness;

import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;

public interface PicBussiness {

    boolean share(Pic pic, StateEnum state);
}
