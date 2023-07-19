package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.dto.UserPicCountDto;

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
     * 根据uuid查询图片详情
     *
     * @param uuid 图片uuid
     * @return 图片详情
     */
    Pic getByUuidOnly(String uuid);

    /**
     * 根据uuid和用户id查询图片详情
     *
     * @param uuid   图片uuid
     * @param userId 用户id
     * @return 图片详情 / 图片与用户不匹配返回null
     */
    Pic getByUuidAndUserId(String uuid, Long userId);

    /**
     * 根据uuid、用户id、状态查询图片详情
     *
     * @param uuid   图片uuid
     * @param userId 用户id
     * @param status 图片状态
     * @return 图片详情 / 不匹配返回null
     */
    Pic getByUuidAndUserIdAndStatus(String uuid, Long userId, PicStatusEnum status);

    /**
     * 根据任务id查询图片详情
     *
     * @param taskId 任务id
     * @return 图片详情
     */
    Pic getByTaskId(Long taskId);

    List<Pic> getPicsByUserId(Long userId);

    Long getParentPicIdById(Long id);

    /**
     * 根据用户idList查询图片数量
     *
     * @param userIdList 用户idList
     * @return 图片数量
     */
    List<UserPicCountDto> countByUserIdList(List<Long> userIdList);

    boolean insert(Pic pic);

    boolean updateByUuid(Pic pic);

    boolean deleteByUuid(String uuid);

    boolean delete(Long id);
}
