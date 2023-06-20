package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.mapper.ModelMapper;
import com.huiyu.service.core.service.ModelService;
import org.springframework.stereotype.Service;

/**
 * 模型表(Model)服务实现类
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

}
