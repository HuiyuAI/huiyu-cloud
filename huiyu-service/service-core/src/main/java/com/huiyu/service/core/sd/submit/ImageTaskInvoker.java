package com.huiyu.service.core.sd.submit;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Stopwatch;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.config.Monitor;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.dto.SDResponse;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wAnG
 * @Date 2023-06-13  00:07
 */
@Service
@Slf4j
public class ImageTaskInvoker {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private TaskService taskService;

    @Resource
    private PicService picService;

    public void invokerGenerate(Task task, String ip) {

        SDResponse resp = invokerHttp(task, ip);

        generateEnd(task, resp);
    }


    private SDResponse invokerHttp(Task task, String ip) {
        String url = ip + getSdApi(task);
        log.info("请求Python端生成图片 url: {}, body: {}", url, task.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(task.getBody(), headers);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ResponseEntity<SDResponse> response = restTemplate.postForEntity(url, entity, SDResponse.class);
        Monitor.recordTime("generate_time", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));

        SDResponse resp = response.getBody();
        log.info("Python端生成图片响应结果 response: {}", JacksonUtils.toJsonStr(resp));

        return resp;
    }

    private void generateEnd(Task task, SDResponse resp) {
        // 将任务置为完成
        updateTaskExecuted(task);

        // 更新图片信息
        updatePic(resp);
    }

    private void updateTaskExecuted(Task task) {
        Task TaskDO = Task.builder()
                .id(task.getId())
                .status(TaskStatusEnum.EXECUTED)
                .updateTime(LocalDateTime.now())
                .build();
        taskService.updateById(TaskDO);
    }

    private void updatePic(SDResponse resp) {
        JsonNode data = resp.getData();
        JsonNode info = data.get("info");
        JsonNode parameters = data.get("parameters");

        String resImageUuid = data.get("res_image_uuid").asText();
        Long seed = null;
        Long subseed = null;
        String infotexts;
        String alwaysonScripts;

        if (info.isTextual()) {
            infotexts = info.asText();
        } else {
            seed = info.get("seed").asLong();
            subseed = info.get("subseed").asLong();
            infotexts = info.get("infotexts").get(0).asText();
        }

        if (parameters.isTextual()) {
            alwaysonScripts = parameters.asText();
        } else {
            alwaysonScripts = data.get("parameters").get("alwayson_scripts").toString();
        }

        Pic pic = Pic.builder()
                .uuid(resImageUuid)
                .seed(seed)
                .subseed(subseed)
                .alwaysonScripts(alwaysonScripts)
                .infotexts(infotexts)
                .updateTime(LocalDateTime.now())
                .build();
        picService.updateByUuid(pic);
    }

    public Task findNextTask(String source) {
        // 寻找新的任务放入线程池
        List<Task> taskList = taskService.getByStatus(TaskStatusEnum.UN_EXECUTED, 1, source);
        if (taskList.isEmpty()) {
            return null;
        }
        Task task = taskList.get(0);
        Task taskDo = Task.builder()
                .id(task.getId())
                .status(TaskStatusEnum.IN_QUEUE)
                .updateTime(LocalDateTime.now())
                .build();
        taskService.updateById(taskDo);
        return task;
    }

    private String getSdApi(Task task) {
        return task.getType().getSdApi();
    }

}
