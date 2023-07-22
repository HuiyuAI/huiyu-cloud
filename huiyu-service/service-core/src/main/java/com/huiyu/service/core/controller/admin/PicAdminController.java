package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.query.PicQuery;
import com.huiyu.service.core.service.PicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Naccl
 * @date 2023-07-22
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pic")
public class PicAdminController {
    private final PicService picService;

    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<Pic>> adminPageQuery(PicQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<Pic> pageInfo = picService.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

}
