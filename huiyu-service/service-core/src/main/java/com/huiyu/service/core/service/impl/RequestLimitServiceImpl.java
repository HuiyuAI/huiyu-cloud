package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.RequestLimit;
import com.huiyu.service.core.mapper.RequestLimitMapper;
import com.huiyu.service.core.service.RequestLimitService;
import org.springframework.stereotype.Service;

/**
 * (RequestLimit)服务实现类
 *
 * @author Naccl
 * @date 2023-07-05
 */
@Service
public class RequestLimitServiceImpl extends ServiceImpl<RequestLimitMapper, RequestLimit> implements RequestLimitService {

    @Override
    public void insert(RequestLimit requestLimit) {
        super.baseMapper.insert(requestLimit);
    }
}
