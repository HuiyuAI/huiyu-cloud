package com.huiyu.service.core.service.submit;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wAnG
 * @Date 2023-06-13  00:07
 */
@Service
@Slf4j
public class imageTaskInvoker {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ImageTaskService imageTaskService;

    @Resource
    private TaskService taskService;

    public void invokerGenerate(Task task) {
        insertTask(task);

        invokerHttp(task);

        generateEnd(task);

        findTask();
    }

    private void generateEnd(Task task) {
        // 结束以后将任务置为完成
        String taskId = task.getTaskId();
        // 数据库任务数据则修改状态
        if (StringUtils.isNotBlank(taskId)) {
            Task TaskDO = Task.builder()
                    .taskId(taskId)
                    .userId(task.getUserId())
                    .status(TaskStatusEnum.EXECUTED)
                    .updateTime(LocalDateTime.now())
                    .build();
            // 根据标识更新数据库状态
            taskService.update(TaskDO);
        }
    }

    private void insertTask(Task task) {
        // todo 在这里插入执行记录
        boolean result = taskService.insertTask(task);
    }

    private void findTask() {
        // 寻找新的任务放入线程池
        List<Task> taskList = taskService.getByStatus(0, 1);
        if (taskList.isEmpty()) {
            return;
        }
        Task task = taskList.get(0);
        task.setStatus(TaskStatusEnum.IN_QUEUE);
        taskService.update(task);
        imageTaskService.trySplitTask(task);
    }

    private void invokerHttp(Task task) {
        // todo 调用api
        String url = getUrl();
        ResponseEntity<String> response = restTemplate.postForEntity(url, task.getBody(), String.class);
        String body = response.getBody();
        JSONObject jsonObject = new JSONObject(body);
        JSONArray imageUuidList = jsonObject.getJSONArray("image_uuid_list");
        for (Object uuid : imageUuidList) {
            String imgUrl = "https://huiyucdn.naccl.top/gen/" + uuid + ".jpg";
            log.info("image url: {}", imgUrl);
        }
    }

    private String getUrl() {
        // todo 多数据源操作后续会放在threadLocal里面
        return StringUtils.EMPTY;
    }



}
