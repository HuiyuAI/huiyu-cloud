package com.huiyu.service.core.sd.submit;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.model.vo.SDResponseVo;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.sd.SDTask2PicConverter;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.TaskService;
import com.huiyu.service.core.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wAnG
 * @Date 2023-06-12  23:44
 */
@Service
@Slf4j
public class ImageTaskService {

    private List<ThreadPoolExecutorDecorator> submitRequestExecutorList = new ArrayList<>();

    @Resource
    private TaskService taskService;

    @Resource
    private PicService picService;


    public void trySplitTask(Task task, Dto dto, SDResponseVo sdResponseVo) {
        List<Task> tasks = splitTask(task, dto);

        execGenerate(tasks, task.getExecSource(), sdResponseVo);

        RequestContext.REQUEST_UUID_CONTEXT.remove();
        RequestContext.CMD_CONTEXT.remove();
        RequestContext.MODEL_CONTEXT.remove();
        RequestContext.PARENT_PIC_CONTEXT.remove();
    }

    private List<Task> splitTask(Task task, Dto dto) {
        List<Task> taskList = Lists.newArrayList();
        if (task.getNum() == 1) {
            dto.setResImageUuid(IdUtil.fastUUID());
            dto.setResImageUrlUuid(IdUtil.fastUUID());
            task.setBody(JacksonUtils.toJsonStr(dto));
            taskList.add(task);
            return taskList;
        }
        Integer point = task.getPoint();
        for (int i = 0; i < task.getNum(); i++) {
            Task copyTask = new Task();
            BeanUtil.copyProperties(task, copyTask);

            copyTask.setId(IdUtils.nextSnowflakeId());
            copyTask.setNum(1);
            copyTask.setPoint(point / task.getNum());

            TaskTypeEnum type = task.getType();
            switch (type) {
                case TXT2IMG:
                    Txt2ImgDto txt2ImgDto = (Txt2ImgDto) dto;
                    if (-1 != txt2ImgDto.getSeed() && i != 0) {
                        txt2ImgDto.setSeed(txt2ImgDto.getSeed() + 1);
                    }
                    break;
                case IMG2IMG:
                    Img2ImgDto img2ImgDto = (Img2ImgDto) dto;
                    break;
                default:
                    break;
            }
            dto.setResImageUuid(IdUtil.fastUUID());
            dto.setResImageUrlUuid(IdUtil.fastUUID());
            copyTask.setBody(JacksonUtils.toJsonStr(dto));

            taskList.add(copyTask);
        }
        return taskList;
    }

    public void execGenerate(List<Task> tasks, String taskExecSource, SDResponseVo sdResponseVo) {
        Optional<ThreadPoolExecutorDecorator> executorOptional = submitRequestExecutorList.stream()
                .filter(decorator -> StringUtils.equals(taskExecSource, decorator.getSourceName()))
                .findAny();

        if (!executorOptional.isPresent()) {
            log.error("未分配执行源");
            return;
        }

        // todo 缺少多级队列
        synchronized (executorOptional.get()) {
            executorOptional.ifPresent(executor -> {
                tasks.forEach(taskItem -> {
                    preInvoker(taskItem, sdResponseVo);
                    executor.commit();
                });
            });
        }
    }

    private void preInvoker(Task task, SDResponseVo sdResponseVo) {

        insertTask(task);

        insertPic(task, sdResponseVo);

    }

    private void insertTask(Task task) {
        if (taskService.getById(task.getId()) != null) {
            return;
        }
        taskService.insertTask(task);
    }

    private void insertPic(Task task, SDResponseVo sdResponseVo) {
        Pic pic = SDTask2PicConverter.convert(task);
        if (picService.getByUuidOnly(pic.getUuid()) != null) {
            return;
        }
        String requestUuid = RequestContext.REQUEST_UUID_CONTEXT.get();
        pic.setRequestUuid(requestUuid);
        pic.setStatus(PicStatusEnum.GENERATING);
        picService.insert(pic);

        if (sdResponseVo.getUuid() == null) {
            sdResponseVo.setUuid(pic.getUuid());
            sdResponseVo.setStatus(pic.getStatus());
            sdResponseVo.setPath(pic.getPath());
            sdResponseVo.setWidth(pic.getWidth());
            sdResponseVo.setHeight(pic.getHeight());
        }
    }

    public List<ThreadPoolExecutorDecorator> getSubmitRequestExecutorList() {
        return submitRequestExecutorList;
    }

    public void setSubmitRequestExecutorList(List<ThreadPoolExecutorDecorator> submitRequestExecutorList) {
        this.submitRequestExecutorList = submitRequestExecutorList;
    }
}
