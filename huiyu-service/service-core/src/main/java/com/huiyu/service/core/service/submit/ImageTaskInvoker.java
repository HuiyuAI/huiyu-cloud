package com.huiyu.service.core.service.submit;

import cn.hutool.json.JSONObject;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.huiyu.service.core.config.Monitor;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
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

        invokerHttp(task);

        generateEnd(task);

        findTask();
    }

    private void generateEnd(Task task) {
        // 结束以后将任务置为完成
        Task TaskDO = Task.builder()
                .taskId(task.getTaskId())
                .status(TaskStatusEnum.EXECUTED)
                .updateTime(LocalDateTime.now())
                .build();
        taskService.updateByTaskId(TaskDO);
    }

    private void insertTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            return;
        }
        boolean result = taskService.insertTask(task);
    }

    private void insertPic(Task task) {
        Pic pic = SDTaskConverter.convert(task);
        picService.insert(pic);
    }

    private void findTask() {
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

    private void invokerHttp(Task task) {
        // todo 调用api
        String url = getUrl();

        log.info("request url: {}, body: {}", url, task.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(task.getBody(), headers);
        Stopwatch stopwatch = Stopwatch.createStarted();

        // TODO 接收对象，更新数据库图片信息
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        Monitor.recordTime("generate_time", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        String body = response.getBody();

        log.info("response body: {}", body);

        JSONObject jsonObject = new JSONObject(body);
        String uuid = jsonObject.getJSONObject("data").getStr("res_image_uuid");
        String imgUrl = "https://huiyucdn.naccl.top/gen/" + uuid + ".jpg";
        log.info("image url: {}", imgUrl);

    }

    private String getUrl() {
        // todo 多数据源操作后续会放在threadLocal里面
        return SDAPIConstant.BASE_URL + SDAPIConstant.TXT2IMG;
    }


}
