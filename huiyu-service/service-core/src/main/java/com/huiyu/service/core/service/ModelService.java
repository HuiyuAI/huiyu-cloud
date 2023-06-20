package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.model.vo.ModelVo;

import java.util.List;

/**
 * 模型表(Model)服务接口
 *
 * @author Naccl
 * @date 2023-06-20
 */
public interface ModelService extends IService<Model> {
    /**
     * 查询全部
     */
    List<ModelVo> queryAll();

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
    boolean removeByIds(List<Integer> ids);
}
