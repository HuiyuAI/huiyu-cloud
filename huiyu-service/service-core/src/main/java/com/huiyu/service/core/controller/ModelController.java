package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
import com.huiyu.service.core.convert.ModelConverter;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.vo.ModelVo;
import com.huiyu.service.core.service.ModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 模型表(Model)控制层
 *
 * @author Naccl
 * @date 2023-06-20
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/model")
public class ModelController {

    private final ModelService modelService;

    /**
     * 查全部已启用的模型
     */
    @RequestLimiter(seconds = 60, maxCount = 30)
    @RequestLogger
    @GetMapping("/list")
    public R<List<ModelVo>> queryAll() {
        List<Model> modelList = modelService.queryAll(true);
        List<ModelVo> modelVoList = ModelConverter.INSTANCE.toVOList(modelList);
        return R.ok(modelVoList);
    }

}
