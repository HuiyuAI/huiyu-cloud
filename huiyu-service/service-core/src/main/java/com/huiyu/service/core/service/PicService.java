package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.PicPageDto;

import java.util.List;

public interface PicService extends IService<Pic> {
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<Pic> queryPage(IPage<Pic> page, PicPageDto dto);

    /**
     * 根据uuid和用户id查询图片详情
     *
     * @param uuid   图片uuid
     * @param userId 用户id
     * @return 图片详情 / 图片与用户不匹配返回null
     */
    Pic getByUuidAndUserId(String uuid, Long userId);

    List<Pic> getPicsByUserId(Long userId);

    Long getParentPicIdById(Long id);

    boolean insert(Pic pic);

    boolean updateByUuid(Pic pic);

    boolean deleteByUuid(String uuid);

    Pic getByUuid(String uuid);

    Pic getByUuidOnly(String uuid);

    Pic getByTaskId(Long taskId);
}
