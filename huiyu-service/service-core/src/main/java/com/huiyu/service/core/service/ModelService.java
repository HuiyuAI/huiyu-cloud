package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.model.query.ModelQuery;

import java.util.List;

/**
 * 模型表(Model)服务接口
 *
 * @author Naccl
 * @date 2023-06-20
 */
public interface ModelService extends IService<Model> {
    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<Model> queryPage(IPage<Model> page, ModelQuery query);

    /**
     * 获取模型分类列表
     */
    List<String> getCategoryList();

    /**
     * 查询全部
     */
    List<Model> queryAll(Boolean enabled);

    /**
     * 根据id查询
     */
    Model getById(Long id, Boolean enabled);

    /**
     * 新增
     */
    boolean save(ModelDto dto);

    /**
     * 批量修改
     */
    boolean updateBatchById(List<ModelDto> dtoList);

    /**
     * 批量删除
     */
    boolean removeByIds(List<Long> ids);
}
