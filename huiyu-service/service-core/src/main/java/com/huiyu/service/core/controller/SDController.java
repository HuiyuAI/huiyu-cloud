package com.huiyu.service.core.controller;

import cn.hutool.json.JSONUtil;
import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.constant.TaskTypeEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdValidator;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.sd.generate.AbstractImageGenerate;
import com.huiyu.service.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Stable Diffusion功能请求处理
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sd")
public class SDController {

    @Resource
    private List<AbstractImageGenerate> imageGenerates;

    /**
     * 文生图
     *
     * @param cmd Txt2ImgCmd
     * @return 校验成功返回等待消息/校验失败返回错误消息
     */
    @PostMapping("/txt2img")
    public R<?> txt2img(@RequestBody Txt2ImgCmd cmd) {
        // 1. 校验用户积分
        Long userId = JwtUtils.getId();
        cmd.setUserId(userId);
        // 2. 参数校验(数值范围)，描述词违禁词检测
        boolean validate = SDCmdValidator.validate(cmd);
        if (!validate) {
            return R.error("参数错误");
        }

        // 3. 提交任务队列

        imageGenerates.stream()
                .filter(imageGenerate -> imageGenerate.isSupport(cmd))
                .forEach(imageGenerate -> imageGenerate.generate(cmd));
        // 4. 处理用户界面

        return R.ok();
    }

    @Resource
    private TaskService taskService;

    @GetMapping("/test")
    public void test() {
        Txt2ImgDto txt2ImgDto = new Txt2ImgDto();
        txt2ImgDto.setSdModelCheckpoint("1");
        txt2ImgDto.setSdVae("2");
        txt2ImgDto.setPrompt("3");
        txt2ImgDto.setNegativePrompt("4");
        txt2ImgDto.setCfgScale(new BigDecimal("5"));
        txt2ImgDto.setResImageUuid("6");

        Task task = new Task();
        task.setId(1L);
        task.setUserId(1L);
        task.setType(TaskTypeEnum.TXT2IMG);
        task.setBody(txt2ImgDto);
        task.setStatus(TaskStatusEnum.EXECUTED);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setNum(1);
        task.setExecSource("1");
        task.setIsDelete(0);

        log.info("{}", JSONUtil.toJsonStr(task));
        taskService.insertTask(task);

        List<Task> byStatus = taskService.getByStatus(TaskStatusEnum.EXECUTED, 1);
        log.info("{}", JSONUtil.toJsonStr(byStatus));
    }
}
