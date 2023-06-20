package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.query.ModelQuery;
import com.huiyu.service.core.model.vo.ModelVo;
import com.huiyu.service.core.service.ModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 分页
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<ModelVo>> queryByPage(ModelQuery modelQuery, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return R.ok();
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public R<ModelVo> save(@RequestBody Model model) {
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody Model model) {
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public R<?> remove(@RequestBody List<Integer> ids) {
        return R.ok();
    }
}
