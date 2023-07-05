package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.PicExt;

public interface PicExtService {

    PicExt getByPicId(Long picId);

    boolean insert(PicExt picExt);

    boolean update(PicExt picExt);

    boolean deleteByPicId(Long picId);
}
