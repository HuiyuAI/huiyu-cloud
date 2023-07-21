package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.model.query.PointRecordQuery;
import com.huiyu.service.core.service.PointRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Naccl
 * @date 2023-07-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pointRecord")
public class PointRecordAdminController {
    private final PointRecordService pointRecordService;

    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<PointRecord>> adminPageQuery(PointRecordQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<PointRecord> pageInfo = pointRecordService.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }
}
