package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.MessageQueueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 8:56
 * @Description: // TODO
 **/
@RestController
public class TestController {

    @Resource
    private MessageQueueService messageService;

    @GetMapping("/test")
    public R<?> test() {
        messageService.enqueueMessage(Task.builder()
                .body("{'user' : 1}")
                .status(0)
                .url("www.baidu.com")
                .userId(2L)
                .build(), 1);
        return R.ok();
    }
}
