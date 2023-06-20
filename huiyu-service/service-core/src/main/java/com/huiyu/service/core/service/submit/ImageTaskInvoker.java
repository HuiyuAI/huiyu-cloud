package com.huiyu.service.core.service.submit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.huiyu.service.core.config.Monitor;
import com.huiyu.service.core.constant.HuiyuConstant;
import com.huiyu.service.core.constant.PicStatusEnum;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.dto.SDResponse;
import com.huiyu.service.core.sd.SDTaskConverter;
import com.huiyu.service.core.sd.constant.SDAPIConstant;
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
import java.util.Objects;
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
    private ImageTaskService imageTaskService;

    @Resource
    private TaskService taskService;

    @Resource
    private PicService picService;

    public void invokerGenerate(Task task) {
        insertTask(task);

        insertPic(task);

        SDResponse resp = invokerHttp(task);

        generateEnd(task, resp);

        findNextTask();
    }

    private void insertTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            return;
        }
        boolean result = taskService.insertTask(task);
    }

    private void insertPic(Task task) {
        Pic pic = SDTaskConverter.convert(task);
        if (picService.getByUuid(pic.getUuid()) != null) {
            return;
        }
        pic.setStatus(PicStatusEnum.GENERATING);
        picService.insert(pic);
    }

    private SDResponse invokerHttp(Task task) {
        String url = getUrl();
        log.info("请求Python端生成图片 url: {}, body: {}", url, task.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(task.getBody(), headers);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ResponseEntity<SDResponse> response = restTemplate.postForEntity(url, entity, SDResponse.class);
        Monitor.recordTime("generate_time", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));

        SDResponse resp = response.getBody();
        log.info("Python端生成图片响应结果 response: {}", resp);

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
                .taskId(task.getTaskId())
                .status(TaskStatusEnum.EXECUTED)
                .updateTime(LocalDateTime.now())
                .build();
        taskService.updateByTaskId(TaskDO);
    }

    private void updatePic(SDResponse resp) {
        String resImageUuid = resp.getData().get("res_image_uuid").asText();
        Long seed = resp.getData().get("info").get("seed").asLong();
        Long subseed = resp.getData().get("info").get("subseed").asLong();
        String infotexts = resp.getData().get("info").get("infotexts").get(0).asText();
        String alwaysonScripts = resp.getData().get("parameters").get("alwayson_scripts").toString();

        Pic pic = Pic.builder()
                .uuid(resImageUuid)
                .seed(seed)
                .subseed(subseed)
                .alwaysonScripts(alwaysonScripts)
                .infotexts(infotexts)
                .updateTime(LocalDateTime.now())
                .build();
        picService.updateByUuid(pic);

        String imgUrl = HuiyuConstant.cdnUrlGen + resImageUuid + HuiyuConstant.imageSuffix;
        log.info("测试图片生成 url: {}", imgUrl);
    }

    private void findNextTask() {
        // 寻找新的任务放入线程池
        List<Task> taskList = taskService.getByStatus(TaskStatusEnum.UN_EXECUTED, 1);
        if (taskList.isEmpty()) {
            return;
        }
        Task task = taskList.get(0);
        Task taskDo = Task.builder()
                .id(task.getId())
                .status(TaskStatusEnum.IN_QUEUE)
                .build();
        taskService.update(taskDo);
        imageTaskService.execGenerate(Lists.newArrayList(task), task.getExecSource());
    }

    private String getUrl() {
        // todo 多数据源操作后续会放在threadLocal里面
        return SDAPIConstant.BASE_URL + SDAPIConstant.TXT2IMG;
    }


}
