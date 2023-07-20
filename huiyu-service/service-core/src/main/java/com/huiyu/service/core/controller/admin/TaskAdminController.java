package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.query.TaskQuery;
import com.huiyu.service.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Naccl
 * @date 2023-07-20
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/task")
public class TaskAdminController {
    private final TaskService taskService;

    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<Task>> adminPageQuery(TaskQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<Task> pageInfo = taskService.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }
}
