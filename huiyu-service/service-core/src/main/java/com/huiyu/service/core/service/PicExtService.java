package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.PicExt;

public interface PicExtService {

    PicExt getByPicId(Long picId);

    int insertPicExt(PicExt picExt);

    int update(PicExt picExt);
}
