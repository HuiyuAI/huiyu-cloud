package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.Pic;

import java.util.List;

public interface PicService {

    List<Pic> getPicsByUserId(String userId);

    boolean delete(Pic pic);

    Long getAncestorById(Long id);
}
