package com.huiyu.service.core.controller.admin;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型表(Model)控制层
 *
 * @author Naccl
 * @date 2023-07-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/model")
public class ModelAdminController {
    private final ModelService modelService;

    /**
     * 查全部
     */
    @GetMapping("/list")
    public R<List<Model>> queryAll() {
        List<Model> modelList = modelService.queryAll(null);
        return R.ok(modelList);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public R<?> save(@RequestBody ModelDto dto) {
        return R.status(modelService.save(dto));
    }

    /**
     * 批量修改
     */
    @PostMapping("/updateBatchById")
    public R<?> update(@RequestBody List<ModelDto> dtoList) {
        return R.ok(modelService.updateBatchById(dtoList));
    }

    /**
     * 批量删除
     */
    @PostMapping("/removeByIds")
    public R<?> remove(@RequestBody List<Long> ids) {
        return R.ok(modelService.removeByIds(ids));
    }
}
