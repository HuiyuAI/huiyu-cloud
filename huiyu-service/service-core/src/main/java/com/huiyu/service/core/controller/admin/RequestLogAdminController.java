package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.RequestLog;
import com.huiyu.service.core.model.query.RequestLogQuery;
import com.huiyu.service.core.service.RequestLogService;
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
@RequestMapping("/admin/requestLog")
public class RequestLogAdminController {

    private final RequestLogService requestLogService;

    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<RequestLog>> adminPageQuery(RequestLogQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<RequestLog> pageInfo = requestLogService.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }
}
