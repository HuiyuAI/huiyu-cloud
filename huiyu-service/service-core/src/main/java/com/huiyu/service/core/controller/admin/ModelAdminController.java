package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.ModelDto;
import com.huiyu.service.core.model.query.ModelQuery;
import com.huiyu.service.core.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 分页查询
     *
     * @param query    筛选条件
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return 查询结果
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<Model>> queryByPage(ModelQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<Model> pageInfo = modelService.queryPage(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

    /**
     * 获取模型分类列表
     */
    @GetMapping("/categoryList")
    public R<List<String>> getCategoryList() {
        return R.ok(modelService.getCategoryList());
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
