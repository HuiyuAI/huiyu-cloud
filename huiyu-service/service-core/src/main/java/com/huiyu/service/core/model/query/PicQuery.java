package com.huiyu.service.core.model.query;

import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-07-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicQuery {

    private Long id;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 请求uuid
     */
    private String requestUuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 模型id
     */
    private Long modelId;
    /**
     * 状态 0生成中 1已生成 2废弃
     */
    private PicStatusEnum status;
    /**
     * 任务类型
     */
    private TaskTypeEnum type;
    /**
     * 质量
     */
    private ImageQualityEnum quality;
    /**
     * 比例
     */
    private String ratio;
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
}
