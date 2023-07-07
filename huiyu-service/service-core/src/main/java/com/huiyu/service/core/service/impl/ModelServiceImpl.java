package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.mapper.ModelMapper;
import com.huiyu.service.core.convert.ModelConverter;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.model.query.ModelQuery;
import com.huiyu.service.core.service.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模型表(Model)服务实现类
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Override
    public IPage<Model> queryPage(IPage<Model> page, ModelQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, Model::getId, query.getId())
                .like(StringUtils.isNotEmpty(query.getName()), Model::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getCategory()), Model::getCategory, query.getCategory())
                .orderByAsc(Model::getPriority)
                .page(page);
    }

    @Override
    public List<String> getCategoryList() {
        return super.lambdaQuery()
                .select(Model::getCategory)
                .groupBy(Model::getCategory)
                .list()
                .stream().map(Model::getCategory).collect(Collectors.toList());
    }

    @Override
    public List<Model> queryAll(Boolean enabled) {
        return super.lambdaQuery()
                .eq(enabled != null, Model::getEnabled, enabled)
                .orderByAsc(Model::getPriority)
                .list();
    }

    @Override
    public Model getById(Long id, Boolean enabled) {
        return super.lambdaQuery()
                .eq(Model::getId, id)
                .eq(enabled != null, Model::getEnabled, enabled)
                .one();
    }

    @Override
    public boolean save(ModelDto dto) {
        Model model = ModelConverter.INSTANCE.toEntity(dto);
        LocalDateTime now = LocalDateTime.now();
        model.setEnabled(0);
        model.setCreateTime(now);
        model.setUpdateTime(now);
        model.setIsDelete(0);
        return super.save(model);
    }

    @Override
    public boolean updateBatchById(List<ModelDto> dtoList) {
        List<Model> modelList = ModelConverter.INSTANCE.toEntityList(dtoList);
        LocalDateTime now = LocalDateTime.now();
        modelList.forEach(model -> model.setUpdateTime(now));
        return super.updateBatchById(modelList);
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        return super.removeByIds(ids);
    }

}
