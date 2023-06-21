package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.Pic;

import java.util.List;

public interface PicService {

    List<Pic> getPicsByUserId(Long userId);

    Long getParentPicIdById(Long id);

    boolean insert(Pic pic);

    boolean updateByUuid(Pic pic);

    boolean deleteByUuid(String uuid);

    Pic getByUuid(String uuid);

    Pic getByUuidNotStatus(String uuid);
}
