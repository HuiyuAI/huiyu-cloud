package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.PicDto;

import java.util.List;

public interface PicService extends IService<Pic> {
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<Pic> queryPage(IPage<Pic> page, PicDto dto);

    List<Pic> getPicsByUserId(Long userId);

    Long getParentPicIdById(Long id);

    boolean insert(Pic pic);

    boolean updateByUuid(Pic pic);

    boolean deleteByUuid(String uuid);

    Pic getByUuid(String uuid);

    Pic getByUuidOnly(String uuid);

    Pic getByTaskId(Long taskId);
}
